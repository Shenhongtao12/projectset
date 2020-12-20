package com.sht.shoesboot.service;

import com.alibaba.fastjson.JSON;
import com.sht.shoesboot.entity.Goods;
import com.sht.shoesboot.mapper.GoodsMapper;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
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
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

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
    private RestHighLevelClient restHighLevelClient;

    /**
     * 解析数据放入es索引库中
     * @return
     */
    public Boolean parseGoods(List<Goods> goods) throws Exception {
        // 把查询到的数据放入es
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("5m");

        for (int i = 0; i < goods.size(); i++) {
            bulkRequest.add(new IndexRequest("shoes_goods")
                    .source(JSON.toJSONString(goods.get(i)), XContentType.JSON));
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }

    /**
     * 分页查询数据
     */
    public List<Map<String, Object>> findByPage(int page, int size, String keyword) throws IOException {
        if (page <= 1) {
            page = 1;
        }
        SearchRequest searchRequest = new SearchRequest("shoes_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //分页
        sourceBuilder.from(page);
        sourceBuilder.size(size);
        //matchQuery
        if (StringUtils.isNoneEmpty(keyword)) {
            MatchQueryBuilder termQueryBuilder = QueryBuilders.matchQuery("title", keyword);
            sourceBuilder.query(termQueryBuilder);
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
        return list;
    }

    public void save(Goods goods) {
        goodsMapper.insertSelective(goods);
    }

}
