package com.zyc.elasticsearch;

import com.zyc.utils.ESUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.junit.Test;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: zyc2913@163.com
 * @Date: 2020/9/3 17:18
 * @Version: 1.0
 * @Description: 索引的创建，删除，判断存在的操作方法
 */
public class Index {

    //从工具类引入客户端,为了便于引用，把其放在最外面，作为全局变量
    RestHighLevelClient client = ESUtils.getClient();
    //创建索引
    @Test
    public void createIndex()throws IOException{
        /**
         * 1.获取“创建索引”请求对象，在请求对象中添加索引名称
         */
        CreateIndexRequest request = new CreateIndexRequest("douyin_1");
        /**
         * 2.通过客户端发送请求对象
         */
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        String index = createIndexResponse.index();
        //控制台返回douyin_1表示创建索引成功
        System.out.println(index);
    }
    //删除索引
    @Test
    public void deleteIndex()throws IOException {
        /**
         * 获取“删除索引”的请求对象,在请求对象中添加索引名称
         */
        DeleteIndexRequest request = new DeleteIndexRequest("douyin_1");
        /**
         * 通过客户端发送请求对象
         */
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        boolean isSuccessful = delete.isAcknowledged();
        //控制台返回true表示删除索引成功
        System.out.println(isSuccessful);
    }

    //返回索引是否存在
    @Test
    public void existIndex()throws IOException{
        /**
         *
         */
        GetIndexRequest request = new GetIndexRequest("douyin_1");
        boolean exists = client.indices().exists(request,RequestOptions.DEFAULT);
        //如果存在，控制台返回true；如果不存在，控制台返回false。
        System.out.println(exists);
    }

}
