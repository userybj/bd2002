package com.xahj.utils;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * Created with IntelliJ IDEA.
 * Author: Amos
 * E-mail: amos@amoscloud.com
 * Date: 2020/9/3
 * Time: 11:15
 * Description:
 */
public class ESUtils {
    private static RestHighLevelClient client = null;

    private static void initClient() {
        client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("bd0201", 9200, "http"),
                new HttpHost("bd0202", 9200, "http"),
                new HttpHost("bd0203", 9200, "http")
        ));
    }

    public static RestHighLevelClient getClient() {
        if (client == null) initClient();
        return client;
    }

}
