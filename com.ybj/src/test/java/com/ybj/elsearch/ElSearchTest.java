package com.ybj.elsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElSearchTest {
    //创建RestHighLevelClient对象
    static RestHighLevelClient client;
    @Before
    public void creatClient(){
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("bd01",9200,"http"),
                        new HttpHost("bd02",9200,"http"),
                        new HttpHost("bd03",9200,"http")));
    }

    //创建索引
    @Test
    public void creatIndex() throws IOException {
        //1.获取“创建索引”请求对象，在请求对象中添加索引名称
        CreateIndexRequest request = new CreateIndexRequest("tiktok");
        //2.通过客户端发送请求对象
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        //3.打印结果
        System.out.println(response.index());
    }
    //删除索引
    @Test
    public void deleteIndex() throws IOException {
        //1.获取“删除”请求对象，在请求对象中添加索引名称
        DeleteIndexRequest request = new DeleteIndexRequest("tiktok");
        //2.通过客户端发送请求对象
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        //返回是否删除成功信息
        boolean deleteMesage = delete.isAcknowledged();
        //3.打印结果
        System.out.println(deleteMesage);
    }
    //返回索引是否存在
    @Test
    public void indexExists() throws IOException {
        //1.获取“查询”请求对象，在请求对象中添加索引名称
        GetIndexRequest request = new GetIndexRequest("tiktok");
        //2.通过客户端发送请求对象
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        //返回输出结果
        System.out.println(exists);
    }
    //批量插入数据1
    @Test
    public void insert() throws IOException {
        //配置超时时间,获取批量插入对象
        BulkRequest timeout = new BulkRequest().timeout("5s");
        //创建要插入的数据
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","Tom");
        jsonObject.put("id",2);
        jsonObject.put("age",20);
        //构建一个索引请求对象,并添加数据
        IndexRequest request = new IndexRequest("student").source(JSON.toJSONString(jsonObject), XContentType.JSON);
        //将索引对象添加批量插入对象
        BulkRequest bulkRequest = timeout.add(request);
        //用客户端提交请求
        BulkResponse response = client.bulk(timeout, RequestOptions.DEFAULT);
        //显示提交请求是否失败,false表示成功
        System.out.println(response.hasFailures());
        //返回提交请求后状态,OK表示成功
        System.out.println(response.status());
        //释放资源
        client.close();
    }
    //批量插入数据2
    @Test
    public void addMessages() throws IOException {
        //配置超时时间,获取批量插入对象
        BulkRequest timeout = new BulkRequest().timeout("5s");
        //读取数据
        //将表格数据转换为制表符分割的txt文件
        List<String> list = FileUtils.readLines(new File("C:\\Users\\ASUS\\Desktop\\douyin_feeds_2019_sample(1).txt"));
        JSONObject jsonObject = new JSONObject();
        String[] title = null;
        for (int i = 0; i < list.size(); i++) {
            if(i == 0){
                title = list.get(i).split("\t");
            }else {
                String[] strs = list.get(i).split("\t");
                for (int j = 0; j < title.length; j++) {
                    String key = title[j];
                    Object value = null;
                    if ("digg_count".equals(title[j]) ||
                            "comment_count".equals(title[j]) ||
                            "share_count".equals(title[j]) ||
                            "gender".equals(title[j])){
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
                //构建一个索引请求对象,并添加数据
                IndexRequest request = new IndexRequest("tiktok").source(JSON.toJSONString(jsonObject),XContentType.JSON);
                //将索引对象添加批量插入对象
                timeout.add(request);
            }
        }
        //用客户端提交请求
        BulkResponse response = client.bulk(timeout, RequestOptions.DEFAULT);
        //显示提交请求是否失败,false表示成功
        System.out.println(response.hasFailures());
        //返回提交请求后状态,OK表示成功
        System.out.println(response.status());
        //释放资源
        client.close();
        //System.out.println(jsonObject);
    }
    //更新文档
    @Test
    public void update() throws IOException {
        //获取“更改”请求对象，在请求对象中添加索引名称以及要更改的id
        UpdateRequest request = new UpdateRequest("student", "1");
        request.timeout("3s");
        //创建要更改的数据
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","Jack");
        jsonObject.put("id",3);
        jsonObject.put("age",12);
        //更改数据
        request.doc(JSON.toJSONString(jsonObject),XContentType.JSON);
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }
}
