package com.xahj.hadoop.mapreduce.wordcount;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: Amos
 * E-mail: amos@amoscloud.com
 * Date: 2020/9/10
 * Time: 9:06
 * Description:
 */
public class JavaWordCount {
    public static void main(String[] args) throws Exception {
//        HashMap<String, Long> map = wc();

        System.out.println(Long.MAX_VALUE);


    }

    private static HashMap<String, Long> wc() throws IOException {
        // 1. 读取数据
        List<String> lines = FileUtils.readLines(new File("C:\\Users\\Administrator\\Desktop\\1.Harry Potter and the Sorcerer's Stone.txt"));

        // 2. 创建保存结果的容器  map
        HashMap<String, Long> map = new HashMap<>();

        // 3. 计数
        for (String line : lines) {
            for (String word : line.split("\\s+")) {
                word = word.replaceAll("\\W", "").toLowerCase();
                if ("".equals(word)) continue;
                if (map.containsKey(word))
                    map.put(word, map.get(word) + 1);
                else map.put(word, 1L);
            }
        }
        return map;
    }
}
