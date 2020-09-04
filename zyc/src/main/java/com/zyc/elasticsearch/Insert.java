package com.zyc.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.zyc.pojo.User;
import com.zyc.utils.ESUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA-2020.2.1.
 *
 * @Author: zyc2913@163.com
 * @Date: 2020/9/3 19:00
 * @Version: 1.0
 * @Description: list集合批量数据的导入和修改更新
 */
public class Insert {
    //从工具类引入客户端,为了便于引用，把其放在最外面，作为全局变量
    RestHighLevelClient client = ESUtils.getClient();

    //批量数据导入
    @Test
    public void bulkRequest() throws IOException{
        BulkRequest bulkRequest = new BulkRequest().timeout("5s");
        //创建一个集合存放user对象
        List<User> users = new ArrayList<>();
        //创建user对象
        User user1 = new User(001,"亚瑟",15,"男","西安市雁塔区");
        User user2 = new User(002,"李白",11,"男","西安市未央区");
        User user3 = new User(003,"鲁班",12,"男","西安市莲湖区");
        User user4 = new User(004,"典韦",14,"男","北京市石景山区");
        User user5 = new User(005,"王昭君",13,"女","成都市武侯区");
        User user6 = new User(006,"瑶",15,"女","西安市雁塔区");
        //将user对象添加到集合中
        users.add(0,user1);
        users.add(1,user2);
        users.add(2,user3);
        users.add(3,user4);
        users.add(4,user5);
        users.add(5,user6);
        for (User user:users) {
            //通过遍历把集合添加到索引以Json串形式
            bulkRequest.add(new IndexRequest("test1").id("1")
                    .source(JSON.toJSONString(user), XContentType.JSON));
        }
        //通过客户端发送请求对象
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        //控制台返回false，表示导入成功，返回true表示导入失败
        System.out.println(bulkResponse.hasFailures());
        //控制台返回OK表示状态正常
        System.out.println(bulkResponse.status());
    }

    //更新文档
    @Test
    public void updateDocument() throws IOException{
        //获得“更新文档”的请求对象，在请求对象中添加索引和id
        UpdateRequest request = new UpdateRequest("test1", "1");
        request.timeout("1s");
        //更新文档的内容
        User user3 = new User(003,"韩信",16,"男","广州市");
        //将更新的文档转成Json串
        request.doc(JSON.toJSONString(user3), XContentType.JSON);
        //通过客户端发送请求
        UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
        //控制台返回更新后的状态：返回OK表示更新成功
        System.out.println(updateResponse.status());
    }

}
