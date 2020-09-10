package com.zyc.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.utils.ESUtils;
import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA-2020.2.1.
 *
 * @Author: zyc2913@163.com
 * @Date: 2020/9/3 13:57
 * @Version: 1.0
 * @Description: elasticsearch的基本操作
 */
public class DouyinBulkInsert {

    public static void main(String[] args) throws Exception{
        //通过工具类引入客户端，放在前面作为全局变量，便于调用
        RestHighLevelClient client = ESUtils.getClient();
        System.out.println(client);
        /**
         *
         * 批量插入
         */
        //1.获取批量插入的对象,该对象放在循环体外定义，便于调用
        BulkRequest bulk = new BulkRequest().timeout("5s");
        String[] fields = null;

        /**
         * 读取数据
         */
        List<String> lines = FileUtils.readLines(new File("D:\\Java\\douyin_feeds_2019_sample.csv"));

        for (int i = 0; i < lines.size(); i++) {
            if (i == 0){
                fields = lines.get(i).split("\t");
            }else {
                /**
                 * 文本数据装换成Json串
                 */
                JSONObject jsonObject = new JSONObject();
                String[] strs = lines.get(i).split("\t");
                for (int j = 0; j < fields.length; j++) {
                    String key = fields[j];
                    Object value = null;
                    if ("digg_count".equals(fields[j])||
                            "comment_count".equals(fields[j])||
                            "share_count".equals(fields[j])||
                            "gender".equals(fields[j])
                    ){
                        try {
                            value = Long.parseLong(strs[j].trim());
                        }catch (Exception e){
                            System.out.println(strs[j]);
                            value = 0L;
                        }

                    }else {
                        value = strs[j];
                    }
                    jsonObject.put(key,value);
                }
                /**
                 * 构建一个索引请求对象
                 */
                IndexRequest request = new IndexRequest("douyin_1")
                        //给索引对象添加数据
                        .source(JSON.toJSONString(jsonObject), XContentType.JSON
                );
                //将索引对象添加到批量插入对象
                bulk.add(request);
            }
        }
        /**
         * 用客户端提交请求
         */
        BulkResponse responses = client.bulk(bulk, RequestOptions.DEFAULT);
        /**
         * 验证是否失败：控制台返回false表示导入成功
         * 查看提交后的状态：控制台返回OK表示状态良好
         */
        System.out.println(responses.hasFailures());
        System.out.println(responses.status());
       //释放资源
        client.close();


    }


}
