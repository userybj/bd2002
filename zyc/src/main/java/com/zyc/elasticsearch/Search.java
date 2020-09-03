package com.zyc.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.zyc.utils.ESUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: zyc2913@163.com
 * @Date: 2020/9/3 22:23
 * @Version: 1.0
 * @Description: 数据查询
 */
public class Search {

    //从工具类引入客户端,为了便于引用，把其放在最外面，作为全局变量
    RestHighLevelClient client = ESUtils.getClient();

    //查询
    @Test
    public void search() throws IOException{
        //获得“搜索查询”的请求对象，并在请求对象中添加索引
        SearchRequest request = new SearchRequest("test1");
        //构建搜索查询条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        /**
         * SearchRequest 搜索请求
         * SearchSourceBuilder 条件构造
         * HighlightBuilder 构建高亮
         * ermQueryBuilder 精确查询
         * MatchAllQueryBuilder .....
         */
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        sourceBuilder.query(matchAllQueryBuilder).timeout(new TimeValue(60, TimeUnit.SECONDS));
        request.source(sourceBuilder);
        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse.getHits(),true));
        System.out.println("*********************************************");
        for (SearchHit documentFields : searchResponse.getHits()) {
            System.out.println(documentFields.getSourceAsMap());
        }

    }

}
