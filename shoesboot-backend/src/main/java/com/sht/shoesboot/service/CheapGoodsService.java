package com.sht.shoesboot.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sht.shoesboot.entity.CheapGoods;
import com.sht.shoesboot.entity.Goods;
import com.sht.shoesboot.entity.PageResult;
import com.sht.shoesboot.kafka.KafkaProducer;
import com.sht.shoesboot.mapper.CheapGoodsMapper;
import com.sht.shoesboot.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Aaron
 * @date 2021/1/29 22:02
 */
@Service
public class CheapGoodsService {

    @Autowired
    private CheapGoodsMapper cheapGoodsMapper;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private RedisService redisService;

    public RestResponse setup(CheapGoods cheapGoods) {
        try {
            Goods goods = goodsService.findByID(cheapGoods.getGoodsId());
            if (goods != null && goods.getShelf()) {
                goods.setPrice(cheapGoods.getPrice());
                Boolean sendKafKa = sendKafKa(goods);
                if (!sendKafKa) {
                    return new RestResponse(400, "网络异常");
                }
                if (!cheapGoodsMapper.existsWithPrimaryKey(cheapGoods.getGoodsId())) {
                    cheapGoodsMapper.insertSelective(cheapGoods);
                }else {
                    cheapGoodsMapper.updateByPrimaryKeySelective(cheapGoods);
                }
                return new RestResponse(200, "");
            }
            return new RestResponse(400, "设置失败，该商品不存在");
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponse(400, "设置失败，服务处理异常");
        }
    }

    public Boolean sendKafKa(Goods goods) {
        try {
            kafkaProducer.send(goods);
            redisService.setData("shoes_goods_" + goods.getId(), goods.getInventory().toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void delete(Integer id) {
        if (cheapGoodsMapper.existsWithPrimaryKey(id)){
            Goods goods = goodsService.findByID(id);
            sendKafKa(goods);
            cheapGoodsMapper.deleteByPrimaryKey(id);
        }
    }

    public PageResult<Goods> queryPage(Boolean status, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        Page<Goods> goodsPage = (Page<Goods>) cheapGoodsMapper.queryPage(status);
        return new PageResult<>(goodsPage.getTotal(), goodsPage.getPages(), goodsPage.getResult());
    }
}
