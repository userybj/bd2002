package com.ybj.hadoop.mapreduce;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class JavaCountWords {
    public static void main(String[] args) throws IOException {
        //1.读取数据
        List<String> lines = FileUtils.readLines(new File("C:\\Users\\ASUS\\Desktop\\LICENSE.txt"));
        //2.创建存储数据的容器
        HashMap<String, Long> map = new HashMap<String, Long>();
        //3.计数
        for (String line : lines) {
            for (String word : line.split("\\s+")) {
                word = word.replaceAll("\\W","").toLowerCase();
                if(!"".equals(word)){
                    if(map.containsKey(word)){
                        map.put(word,map.get(word)+1);
                    }else {
                        map.put(word,1L);
                    }
                }
            }
        }
        System.out.println(map);
    }
}
