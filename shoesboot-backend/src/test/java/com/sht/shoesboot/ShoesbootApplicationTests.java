package com.sht.shoesboot;

import com.alibaba.fastjson.JSON;
import com.sht.shoesboot.entity.Goods;
import com.sht.shoesboot.mapper.GoodsMapper;
import com.sht.shoesboot.service.GoodsService;
import lombok.experimental.PackagePrivate;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
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

    //测试索引是否存在
    @Test
    void testExistIndex() throws IOException {
        //1.创建索引的请求
        GetIndexRequest request = new GetIndexRequest("shoes_goods");
        //2客户端执行请求，请求后获得响应
        boolean exist =  restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println("测试索引是否存在-----"+exist);
    }

    @Test
    public void testDeleteIndex() throws Exception{
        DeleteIndexRequest deleteRequest = new DeleteIndexRequest("shoes_goods");
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println("删除索引--------"+delete.isAcknowledged());
    }

    @Test
    public void saveEs() throws Exception {
        List<Goods> goods = goodsMapper.selectAll();
        Boolean aBoolean = goodsService.parseGoods(goods);
        System.out.println(aBoolean);
    }

    @Test
    public void testQueryEs() throws IOException {
        System.out.println(goodsService.findByPage(1, 30, "nike"));
    }

}
