package com.sht.shoesboot.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sht.shoesboot.entity.Goods;
import com.sht.shoesboot.entity.GoodsHistory;
import com.sht.shoesboot.entity.PageResult;
import com.sht.shoesboot.mapper.GoodsHistoryMapper;
import com.sht.shoesboot.mapper.GoodsMapper;
import com.sht.shoesboot.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Aaron
 * @date 2020/12/19 23:00
 */
@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsHistoryMapper historyMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 解析数据放入es索引库中
     * @return
     */
    public Boolean parseGoods(List<Goods> goods) throws Exception {
        // 把查询到的数据放入es
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("5m");

        for (Goods goodsTmp : goods) {
            String a = goodsTmp.getTitle().substring(0, goodsTmp.getTitle().length() / 2).trim();
            String b = goodsTmp.getTitle().substring(goodsTmp.getTitle().length() / 2).trim();
            if (a.equals(b)){
                goodsTmp.setTitle(a);
            }
            bulkRequest.add(new IndexRequest("shoes_goods")
                    .id(goodsTmp.getId().toString())
                    .source(JSON.toJSONString(goodsTmp), XContentType.JSON));
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }

    public void saveGoodsToEs(JSONObject goods) {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("1s");
        bulkRequest.add(new IndexRequest("shoes_goods")
                .id(goods.get("id").toString())
                .source(goods, XContentType.JSON));
        try {
            BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 分页查询数据
     */
    public PageResult<Map<String, Object>> findByPage(int page, int size, String keyword) throws IOException {
        if (page <= 1) {
            page = 1;
        }
        SearchRequest searchRequest = new SearchRequest("shoes_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //分页
        sourceBuilder.from((page - 1) * size);
        sourceBuilder.size(size);
        //matchQuery
        if (StringUtils.isNoneEmpty(keyword)) {
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", keyword);
            sourceBuilder.query(matchQueryBuilder);
        }
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        //多个高亮显示
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);

        //执行搜索
        searchRequest.source(sourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //解析结果
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : search.getHits().getHits()) {
            //解析高亮的字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField title = highlightFields.get("title");
            //原来的结果
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            if (title != null) {
                Text[] fragments = title.fragments();
                String newTitle = "";
                for (Text text : fragments) {
                    newTitle += text;
                }
                sourceAsMap.put("title", newTitle);
            }

            list.add(sourceAsMap);
        }
        long total = search.getHits().getTotalHits().value;
        int pages = Math.toIntExact(total) / size;
        return new PageResult<>(total, pages + 1, list);
    }

    public int save(Goods goods) {
        goodsMapper.insertSelective(goods);
        return goods.getId();
    }

    public Map<String, Object> findById(Integer id) {
        GetRequest getRequest = new GetRequest("shoes_goods", id.toString());
        GetResponse response = null;
        try {
            response = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response != null ? response.getSourceAsMap() : null;
    }

    public Boolean soldOut(Integer id) {
        DeleteRequest deleteRequest = new DeleteRequest("shoes_goods", id.toString());
        deleteRequest.timeout("2s");
        try {
            Goods goods = new Goods();
            goods.setId(id);
            goods.setShelf(false);
            goodsMapper.updateByPrimaryKeySelective(goods);
            DeleteResponse delete = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            return delete.status() != RestStatus.NOT_FOUND;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int update(Goods goods) {
        return goodsMapper.updateByPrimaryKeySelective(goods);
    }

    public boolean existsWithPrimaryKey(Integer id) {
        return goodsMapper.existsWithPrimaryKey(id);
    }

    public void delete(Integer id) {
        Goods goods = goodsMapper.selectByPrimaryKey(id);
        goodsMapper.deleteByPrimaryKey(id);
        goods.setId(id);
        historyMapper.insertSelective(new GoodsHistory(goods));
    }

    public List<Goods> queryShelfGoods(Integer userId) {
        return goodsMapper.queryShelfGoods(userId);
    }

    public Goods findByID(Integer id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    public PageResult<Goods> findGoodsPage(String keyword, Boolean shelf, Integer page, Integer size) {
        Example example = new Example(Goods.class);
        Example.Criteria criteria = example.createCriteria();
        if (shelf != null) {
            criteria.andEqualTo("shelf", shelf);
        }

        if (keyword != null) {
            criteria.andLike("title", "%" + keyword + "%");
        }
        PageHelper.startPage(page, size);
        Page<Goods> goodsPage = (Page<Goods>) goodsMapper.selectByExample(example);
        return new PageResult<>(goodsPage.getTotal(), goodsPage.getPages(), goodsPage.getResult());
    }
}
