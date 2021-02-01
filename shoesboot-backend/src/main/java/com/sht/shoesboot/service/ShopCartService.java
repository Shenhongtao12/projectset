package com.sht.shoesboot.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sht.shoesboot.DTO.ShopCartDTO;
import com.sht.shoesboot.entity.Favorite;
import com.sht.shoesboot.entity.Goods;
import com.sht.shoesboot.entity.PageResult;
import com.sht.shoesboot.entity.ShopCart;
import com.sht.shoesboot.mapper.ShopCartMapper;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Aaron
 * @date 2020/12/27 23:40
 */
@Service
public class ShopCartService {

    @Autowired
    private ShopCartMapper shopCartMapper;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private RedisService redisService;

    public void update(ShopCart shopCart) {
        shopCartMapper.updateByPrimaryKeySelective(shopCart);
    }

    public PageResult<ShopCartDTO> queryPage(Integer userId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        Example example = new Example(ShopCart.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        example.setOrderByClause("id DESC");
        Page<ShopCart> carts = (Page<ShopCart>) shopCartMapper.selectByExample(example);
        Map<Integer, ShopCart> map = carts.getResult().stream().collect(Collectors.toMap(ShopCart::getGoodsId, shopCart -> shopCart));
        List<ShopCartDTO> shopCartDto = new ArrayList<>();

        if (carts.getTotal() > 0) {
            MultiGetRequest request = new MultiGetRequest();
            for (ShopCart shopCart : carts.getResult()) {
                request.add(new MultiGetRequest.Item("shoes_goods", shopCart.getGoodsId().toString()));
            }
            try {
                MultiGetResponse itemResponses = restHighLevelClient.mget(request, RequestOptions.DEFAULT);
                for (MultiGetItemResponse respons : itemResponses.getResponses()) {
                    Goods goods = new Goods();
                    if (respons.getResponse().isExists()) {
                        goods = JSON.parseObject(JSON.toJSONString(respons.getResponse().getSource()), Goods.class);
                        String inventory = redisService.getData("shoes_goods_" + goods.getId());
                        shopCartDto.add(new ShopCartDTO(goods.getId(), goods.getTitle(), goods.getImages(), goods.getPrice(), map.get(goods.getId()).getId(),  map.get(goods.getId()).getAmount(), Integer.parseInt(inventory), map.get(goods.getId()).getCheck()));
                    } else {
                        // flag = true;
                        goods = favoriteService.queryGoods(Integer.valueOf(respons.getResponse().getId()));
                        shopCartDto.add(new ShopCartDTO(goods.getId(), goods.getTitle(), goods.getImages(), goods.getPrice(), map.get(goods.getId()).getId(),  map.get(goods.getId()).getAmount(), goods.getInventory() != null ? goods.getInventory() : 0, map.get(goods.getId()).getCheck()));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        shopCartDto.forEach(s -> {
            s.setImage(s.getImage().substring(0, s.getImage().indexOf(",")));
        });
        return new PageResult<>(carts.getTotal(), carts.getPages(), shopCartDto);
    }

    public Integer save(ShopCart shopCart) {
        shopCart.setCheck(false);
        return shopCartMapper.insertSelective(shopCart);
    }

    public Boolean delete(Integer id) {
        if (shopCartMapper.existsWithPrimaryKey(id)){
            shopCartMapper.deleteByPrimaryKey(id);
            return true;
        }
        return false;
    }

    public ShopCart checkCar(Integer goodsId, Integer userId) {
        Example example = new Example(ShopCart.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("goodsId", goodsId);
        criteria.andEqualTo("userId", userId);
        return shopCartMapper.selectOneByExample(example);
    }
}