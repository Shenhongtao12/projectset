package com.sht.shoesboot;

import com.alibaba.fastjson.JSON;
import com.sht.shoesboot.entity.Goods;
import com.sht.shoesboot.mapper.GoodsMapper;
import com.sht.shoesboot.service.GoodsService;
import lombok.experimental.PackagePrivate;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ShoesbootApplicationTests {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    void testCreateIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("shoes_goods");
        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(response));
    }

    @Test
    public void saveEs() throws Exception {
        List<Goods> goods = goodsMapper.selectAll();
        Boolean aBoolean = goodsService.parseGoods(goods);
        System.out.println(aBoolean);
    }

    @Test
    public void testQueryEs() throws IOException {
        List<Map<String, Object>> nike = goodsService.findByPage(1, 30, "nike");
        System.out.println(nike);
    }

}
