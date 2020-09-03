

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
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
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created with IntelliJ IDEA.
 * Author: Amos
 * E-mail: amos@amoscloud.com
 * Date: 2020/9/3
 * Time: 8:43
 * Description:
 */
public class ElasticSearchConfig {

    static RestHighLevelClient client;

    @Before
    public void client() throws IOException {
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("bd0201", 9200, "http"),
                        new HttpHost("bd0202", 9200, "http"),
                        new HttpHost("bd0203", 9200, "http")
                ));
    }

    //创建索引
    @Test
    public void createIndex() throws IOException {
        //1.获取“创建索引”请求对象，在请求对象中添加索引名称
        CreateIndexRequest request = new CreateIndexRequest("douyin_1");
        //2.通过客户端发送请求对象
        CreateIndexResponse createIndexResponse =
                client.indices().create(request, RequestOptions.DEFAULT);
        //3.打印结果
        System.out.println(createIndexResponse.index());
    }

    //删除索引
    @Test
    public void deleteIndex() throws IOException {
        //1.获取“删除”请求对象，在请求对象中添加索引名称
        DeleteIndexRequest request = new DeleteIndexRequest("douyin_1");
        //2.通过客户端发送请求对象
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        boolean isSuccessful = delete.isAcknowledged();

        System.out.println(isSuccessful);
    }

    //返回索引是否存在
    @Test
    public void existIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("douyin_1");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //删除文档
    @Test
    public void deleteDocument() throws IOException {
        DeleteRequest request = new DeleteRequest("douyin_1", "2");
        request.timeout("1s");
        DeleteResponse deleteResponse = client.delete(request, RequestOptions.DEFAULT);

        System.out.println(deleteResponse.status());
    }


    //查询
    @Test
    public void search() throws IOException {
        SearchRequest request = new SearchRequest("douyin_1");

        //构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // SearchRequest 搜索请求
        // SearchSourceBuilder 条件构造
        // HighlightBuilder 构建高亮
        // TermQueryBuilder 精确查询
        // MatchAllQueryBuilder .....
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        sourceBuilder.query(matchAllQueryBuilder)
                .timeout(new TimeValue(60, TimeUnit.SECONDS));

        request.source(sourceBuilder);
        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse.getHits(), true));
        System.out.println("===================================");
        for (SearchHit documentFields : searchResponse.getHits()) {
            System.out.println(documentFields.getSourceAsMap());
        }

    }

    //批量插入数据
    @Test
    public void BulkRequest() throws IOException {

        BulkRequest bulkRequest = new BulkRequest()
                .timeout("5s");

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");

        List<User> users = Arrays.asList(
                new User("u1", list),
                new User("u2", list),
                new User("u3", list));

        for (User user : users) {
            bulkRequest.add(new IndexRequest("test111")
                    //.id("xxx")
                    .source(JSON.toJSONString(user), XContentType.JSON));
        }

        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        //是否失败,false表示成功
        System.out.println(bulkResponse.hasFailures());
        System.out.println(bulkResponse.status());
    }

    //更新文档
    @Test
    public void updateDocument() throws IOException {
        UpdateRequest request = new UpdateRequest("test01", "1");
        request.timeout("1s");

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");

        User user = new User("u2", list);
        request.doc(JSON.toJSONString(user), XContentType.JSON);
        UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);

        System.out.println(updateResponse.status());
    }

}
