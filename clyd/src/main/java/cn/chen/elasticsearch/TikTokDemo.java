package cn.chen.elasticsearch;

import cn.chen.hadoop.utils.EsUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author:chenliuyiding
 * @date 2020/9/3 13: 15:
 * @description
 **/
public class TikTokDemo {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = EsUtils.getClient();
        System.out.println(client);
        //4. 批量插入
        // 4.1 获取批量插入对象
        BulkRequest bulk = new BulkRequest().timeout("5s");


        //1.读取数据
        List<String> lines = FileUtils.readLines(new File("C:\\Users\\l\\Desktop\\douyin.csv"));
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
                        } catch (Exception e) {
                            System.out.println(strs[j]);
                            value = 0L;
                        }

                    } else {
                        value = strs[j];
                    }

                    jsonObject.put(key, value);
                }
                // 4.2 构建一个索引请求对象
                IndexRequest request = new IndexRequest("tiktok_1")
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
    }

}

