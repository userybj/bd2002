package com.wkj.hdfs;

import com.wkj.utils.HDFSUtils;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.*;


/**
 * 用流的形式上传、下载数据
 */

public class HDFSDemo2 {
    public static void main(String[] args) throws IOException {
        //hdfs的流操作
        //1. 上传文件
       // upload();
        //2. 下载文件
        download();
    }

    private static void download() throws IOException {
        //HDFS  ->  java程序内存   ->    本地文件
        // 1.1 从HDFS开启输入流
        FSDataInputStream fis = HDFSUtils.getFs().open(new Path("/dateformat.docx"));
        // 1.2 获取输出流
        FileOutputStream fos = new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\下载.docx"));
        // 1.3 通过IOUtils连接输入、输出流,buffSize为缓存区大小，true为数据传输完成后关闭io流
        IOUtils.copyBytes(fis,fos,4096,true);
    }

    private static void upload() throws IOException {
        //  本地文件  ->  java程序内存  ->  HDFS
        //获取输入流
        FileInputStream fis = new FileInputStream(new File("F:\\学习资料\\学习笔记\\日期格式转化.docx"));
        //获取桐乡HDFS的输出流
        FSDataOutputStream fos = HDFSUtils.getFs().create(new Path("/dateformat.docx"));
        //连接输入、输出流
        IOUtils.copyBytes(fis,fos,4096,true);
    }
}
