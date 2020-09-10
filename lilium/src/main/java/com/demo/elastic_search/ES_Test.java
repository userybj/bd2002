package com.demo.elastic_search;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.utils.elastic_search.LocalDataLoader;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;

import java.io.File;

public class ES_Test {

    public static void main(String[] args) throws Exception {

        //加载数据到内存，并转换为json对象
        LocalDataLoader loader = new LocalDataLoader(
                new File("F:\\桌面\\douyin_feeds_2019_sample.txt"),
                "utf-8");
        JSONObject data = loader.getJsonData("\t");

        //创建客户端
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.110.10",9200,"http"),
                        new HttpHost("192.168.110.100",9200,"http"),
                        new HttpHost("192.168.110.130",9200,"http")
                )
        );

        //创建“大批量（数据处理）请求”对象
        BulkRequest bulkRequest = new BulkRequest().timeout("4s");

        //为大批量请求对象添加请求
        JSONObject[] JSONs = (JSONObject[])data.get("content");
        System.out.println(JSONs.length);
        for (JSONObject json : JSONs) {
            //把每一行数据作为一个JSON对象，装入索引请求
            String s = JSON.toJSONString(json);
            IndexRequest indexRequest = new IndexRequest("tic_talk")
                    .source(s,XContentType.JSON);
            //System.out.println(s);
            // TODO 试试直接装入JSON对象
            // 把索引请求装入大批量请求对象
            bulkRequest.add(indexRequest);
        }

        // 执行大批量请求
        BulkResponse bulkResp =
                client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulkResp.hasFailures());
        System.out.println("status = " + bulkResp.status().getStatus());

        //关闭客户端
        client.close();
    }

    @Test
    public void ttt(){

    }


}
