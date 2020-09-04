package com.zyc.utils;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * Created with IntelliJ IDEA-2020.2.1.
 *
 * @Author: zyc2913@163.com
 * @Date: 2020/9/3 14:02
 * @Version: 1.0
 * @Description: elasticsearch的工具类 :封装客户端client
 */
public class ESUtils {
   /**
    * 获取客户端
    */
   private static RestHighLevelClient client = null;
   private static void initClient(){
      client = new RestHighLevelClient(RestClient.builder(
              new HttpHost("192.168.9.11",9200,"http"),
              new HttpHost("192.168.9.12",9200,"http"),
              new HttpHost("192.168.9.13",9200,"http")
      ));
   }
   public static RestHighLevelClient getClient(){
      if (client==null) initClient();
      return client;
   }


}
