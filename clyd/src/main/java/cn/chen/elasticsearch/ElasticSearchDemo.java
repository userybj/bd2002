package cn.chen.elasticsearch;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.security.user.User;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author:chenliuyiding
 * @date 2020/9/3 14: 18:
 * @description
 **/
public class ElasticSearchDemo {
    private RestHighLevelClient client = null;

    @Before
    public void Client() {
        client = new RestHighLevelClient(RestClient.builder(
                new org.apache.http.HttpHost("hadoop101", 9200, "http"),
                new org.apache.http.HttpHost("hadoop102", 9200, "http"),
                new HttpHost("hadoop103", 9200, "http")
        ));
    }

    /**
     * 创建索引
     */
    @Test
    public void createIndex() throws IOException {
        // 1.获取“创建索引请求对象”，在请求对象中添加索引名称
        CreateIndexRequest request = new CreateIndexRequest("tiktok_1");
        // 2.通过客户端发送请求索引对象
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse.index());
    }

    /**
     * 删除索引
     */
    @Test
    public void deleteIndex() throws IOException {
        // 1.获取“删除”请求对象，在请求对象中添加索引的名字
        DeleteIndexRequest request = new DeleteIndexRequest("tiktok_1");
        // 2.通过客户端发送请求对象
        AcknowledgedResponse deleteIndexResponse = client.indices().delete(request, RequestOptions.DEFAULT);
        boolean isSuccessful = deleteIndexResponse.isAcknowledged();
        System.out.println(isSuccessful);
    }

    /**
     * 返回索引是否存在
     */
    @Test
    public void existIndex() throws IOException {
        GetIndexRequest tiktok_1 = new GetIndexRequest("tiktok_1");
        boolean exists = client.indices().exists(tiktok_1, RequestOptions.DEFAULT);
        System.out.println(exists);
    }
    /**
     * 批量插入数据
     */
    @Test
    public void BulkRequest() throws IOException {
        BulkRequest bulkRequest = new BulkRequest().timeout("5s");
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        List<User> users = Arrays.asList(
                new User("u1",list),
                new User("u2",list),
                new User("u3",list)
        );
        for (User user : users) {
            bulkRequest.add(new IndexRequest("test11")
                    .source(JSON.toJSONString(user), XContentType.JSON));
        }
        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulk.hasFailures());
        System.out.println(bulk.status());
    }

    @Test
    public void updateDocument() throws IOException {
        UpdateRequest request = new UpdateRequest("test11","OL-2UnQBFBb8g6EoLeQ9");
        request.timeout("1s");
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("5");
        User user = new User("u2", list);
        request.doc(JSON.toJSONString(user),XContentType.JSON);
        UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
        System.out.println(updateResponse.status());
    }

    /**
     * 查询
     */
    @Test
    public void search() throws IOException {
        SearchRequest request = new SearchRequest("test11");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        searchSourceBuilder.query(matchAllQueryBuilder).timeout(new TimeValue(60, TimeUnit.SECONDS));
        SearchRequest source = request.source(searchSourceBuilder);
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        JSON.toJSONString(search.getHits(),true);
        System.out.println("===============");
        for (SearchHit documentFields : search.getHits()) {
            System.out.println(documentFields.getSourceAsMap());
        }
    }




}
