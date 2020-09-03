package com.xahj.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xahj.utils.ESUtils;
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
 * Created with IntelliJ IDEA.
 * Author: Amos
 * E-mail: amos@amoscloud.com
 * Date: 2020/9/3
 * Time: 10:21
 * Description:
 */
public class DouyinBulkInsert {


    static class Person {
        private String name;
        private Integer age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

    public static void main(String[] args) throws Exception {


        RestHighLevelClient client = ESUtils.getClient();
        System.out.println(client);
        //4. 批量插入
        // 4.1 获取批量插入对象
        BulkRequest bulk = new BulkRequest().timeout("5s");


        //1.读取数据
        List<String> lines = FileUtils.readLines(new File("C:\\Users\\Administrator\\Desktop\\douyin_feeds_2019_sample.csv"));
        String[] fields = null;
        for (int i = 0; i < lines.size(); i++) {
            if (i == 0) {
                fields = lines.get(i).split(",");
            } else {

                //2.文本数据转换成Json串
                JSONObject jsonObject = new JSONObject();
                String[] strs = lines.get(i).split(",");
                for (int j = 0; j < fields.length; j++) {
                    String key = fields[j];
                    Object value = null;

                    if ("digg_count".equals(fields[j]) ||
                            "comment_count".equals(fields[j]) ||
                            "share_count".equals(fields[j]) ||
                            "gender".equals(fields[j])) {

                        try {
                            // todo  脏数据的处理待解决
                            value = Long.parseLong(strs[j].trim());
                        }catch (Exception e){
                            System.out.println(strs[j]);
                            value = 0L;
                        }

                    } else {
                        value = strs[j];
                    }

                    jsonObject.put(key, value);
                }
                // 4.2 构建一个索引请求对象
                IndexRequest request = new IndexRequest("douyin_1")
                        // 4.3 给索引对象添加数据
                        .source(JSON.toJSONString(jsonObject), XContentType.JSON);
                //4.4 将索引对象添加批量插入对象
                bulk.add(request);
            }
        }


        //4.5用客户端提交请求
        BulkResponse responses = client.bulk(bulk, RequestOptions.DEFAULT);

        //是否失败,false表示成功
        System.out.println(responses.hasFailures());
        System.out.println(responses.status());



        client.close();




        //   name:amos   age:18
//        Person person = new Person();
//        person.setName("amos");
//        person.setAge(18);

//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("name", "amos");
//        jsonObject.put("age", 18);
//        System.out.println(JSON.toJSONString(jsonObject));

/*



        //3. 获取ES客户端
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("bd0201", 9300, "http"),
                new HttpHost("bd0202", 9300, "http"),
                new HttpHost("bd0203", 9300, "http")
        ));
         */


    }


}
