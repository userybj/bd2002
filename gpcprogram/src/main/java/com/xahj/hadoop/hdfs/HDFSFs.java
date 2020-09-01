package com.xahj.hadoop.hdfs;

import com.xahj.until.HDFSUtil;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HDFSFs {

    public static void main(String[] args) throws Exception {
        FileSystem fs = HDFSUtil.getFs();
        //文件上传 最多可以有四个参数 1.为判断是否删除源文件，默认为否
        //2.判断是否覆盖同名文件，默认为是
        //3.为源路径
        //4.为目标路径
        if (fs!=null){
        //fs.copyFromLocalFile(new Path("C:/Users/Administrator/Desktop/1.txt"),new Path("/1.txt"));
       //除第二个之外同上
        //第四个参数为是否启用本地文件管理系统
       // fs.copyToLocalFile(new Path("/1.txt"),new Path("F:/sec/1.txt") );
       // fs.mkdirs(new Path("/icedata"));
       // fs.mkdirs(new Path("/2020-09-01"));
       // fs.delete(new Path("/icedata"),true);
            boolean rename = fs.rename(new Path("/1.txt"), new Path("/2.txt"));
            System.out.println(rename);
        }
    }
}
