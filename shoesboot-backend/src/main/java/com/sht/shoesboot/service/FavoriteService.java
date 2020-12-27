package com.sht.shoesboot.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sht.shoesboot.entity.Favorite;
import com.sht.shoesboot.entity.Goods;
import com.sht.shoesboot.mapper.FavoriteMapper;
import com.sht.shoesboot.mapper.GoodsHistoryMapper;
import com.sht.shoesboot.mapper.GoodsMapper;
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
import java.util.Date;
import java.util.List;

/**
 * @author Aaron
 * @date 2020/12/24 23:33
 */
@Service
public class FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsHistoryMapper goodsHistoryMapper;

    public Integer exists(Favorite favorite) {
        Example example = new Example(Favorite.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("goods_id", favorite.getGoodsId());
        criteria.andEqualTo("user_id", favorite.getUserId());
        Favorite byExample = favoriteMapper.selectOneByExample(example);
        return byExample != null ? byExample.getId() : 0;
    }

    public void collect(Favorite favorite) {
        Integer flagId = exists(favorite);
        if (flagId != 0) {
            favoriteMapper.deleteByPrimaryKey(flagId);
        } else {
            favorite.setInDate(new Date());
            favoriteMapper.insertSelective(favorite);
        }
    }

    public List<Goods> queryFavorite(Integer userId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        Example example = new Example(Favorite.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("user_id", userId);
        Page<Favorite> favoritePage = (Page<Favorite>) favoriteMapper.selectByExample(example);

        List<Goods> goodsList = new ArrayList<>();

        // Boolean flag = false;
        if (favoritePage.getTotal() > 0) {
            MultiGetRequest request = new MultiGetRequest();
            for (Favorite favorite : favoritePage.getResult()) {
                request.add(new MultiGetRequest.Item("shoes", favorite.getGoodsId().toString()));
            }
            try {
                MultiGetResponse itemResponses = restHighLevelClient.mget(request, RequestOptions.DEFAULT);
                for (MultiGetItemResponse respons : itemResponses.getResponses()) {
                    if (respons.getResponse().isExists()) {
                        goodsList.add(JSON.parseObject(JSON.toJSONString(respons.getResponse().getSource()), Goods.class));
                    } else {
                        // flag = true;
                        Goods goods = goodsMapper.selectByPrimaryKey(respons.getResponse().getId());
                        if (goods == null) {
                            goodsList.add(goodsHistoryMapper.selectByPrimaryKey(respons.getResponse().getId()));
                        }else {
                            goodsList.add(goods);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //下架的商品
//        if (flag) {
//            goodsList.addAll(goodsService.queryShelfGoods(userId));
//        }
        return goodsList;
    }

    public void batchDelete(List<Integer> ids) {
        favoriteMapper.batchDelete(ids);
    }
}
