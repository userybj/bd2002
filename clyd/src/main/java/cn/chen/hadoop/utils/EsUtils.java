package cn.chen.hadoop.utils;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;

import java.io.IOException;

/**
 * @author:chenliuyiding
 * @date 2020/9/3 13: 17:
 * @description
 **/
public class EsUtils {
    private static RestHighLevelClient client = null;

    /**
     * 初始化客户端添加端口信息并添加索引
     */
    private static void initClient() throws IOException {
        client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("hadoop101", 9200, "http"),
                new HttpHost("hadoop102", 9200, "http"),
                new HttpHost("hadoop103", 9200, "http")
        ));

    }

    public static RestHighLevelClient getClient() throws IOException {
        if (client == null) {
            initClient();
        }
        return client;
    }
}
