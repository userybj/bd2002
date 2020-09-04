package com.utils.elastic_search;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONCreator;
import org.apache.avro.data.Json;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LocalDataLoader {
    /** 本地数据源的路径*/
    private File source;
    /** 编码集*/
    private String encoding;
    /** 获取到的数据集 */
    private List<Object>[] lineLists = null;

    /**
     * 获取本地数据加载期
     * @param filePath 数据源的文件路径
     * @param encoding 文件的编码集
     */
    public LocalDataLoader(File filePath,String encoding) {
        source = filePath;
        this.encoding = encoding;
    }

    /**
     * 由文件获取json对象
     * @param separator 文件中每行中字段值的分隔符
     * @return 对应文件格式的json对象
     * @throws IOException 读取文件的IO流异常
     */
    public JSONObject getJsonData(String separator) throws IOException {
        //通过FileUtils工具类的静态方法读入文件。
        List<String> lines = FileUtils.readLines(source,encoding);
        //创建JSON对象，把数据塞进去
        JSONObject json = new JSONObject();

        int size = lines.size();
        System.out.println("size = " + size);
        JSONObject[] jsonArr = new JSONObject[size-1];

        //处理字段行
        String[] t = lines.get(0).split(separator);
        int ln = t.length;
        //遍历数据
        for (int i = 1; i < size; i++) {
            JSONObject thisLine = new JSONObject();
            String[] lineStrs = lines.get(i).split(separator);
            //存放行数据
            for (int j = 0; j < ln; j++) {
                //无需处理数字类型数据，都被识别为字符串了
                thisLine.put(t[j], lineStrs[j]);
            }
            jsonArr[i-1] = thisLine;
        }
        json.put("content", jsonArr);
        return json;
    }

}
