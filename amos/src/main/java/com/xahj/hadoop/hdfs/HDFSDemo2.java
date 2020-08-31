package com.xahj.hadoop.hdfs;

import com.xahj.utils.HDFSUtils;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Author: Amos
 * E-mail: amos@amoscloud.com
 * Date: 2020/8/31
 * Time: 15:14
 * Description:
 */
public class HDFSDemo2 {
    public static void main(String[] args) throws Exception {
        //hdfs的流操作

        //1. 上传文件
//        upload();

        //2. 下载文件
        download();

    }

    private static void download() throws Exception{
        //  HDFS  ->  java程序内存  ->  本地文件
        // 1.1 从HDFS开启输入流
        FSDataInputStream fis = HDFSUtils.getFS().open(new Path("/hadoop-2.7.7.tar.gz"));

        // 1.2 获取输出流
        FileOutputStream fos = new FileOutputStream(new File("C:/Users/Administrator/Desktop/hadoop-2.7.7.tar.gz"));

        // 1.3 拷贝流
        IOUtils.copyBytes(fis,fos,4096,true);


    }

    private static void upload() throws IOException {
        //  本地文件  ->  java程序内存  ->  HDFS

        //1.1 获取输入流
        FileInputStream fis = new FileInputStream(new File("C:/Users/Administrator/Desktop/bd2002/amos/src/main/resources/log4j.properties"));

        //1.2 获取通向HDFS的输出流
        FSDataOutputStream fdo = HDFSUtils.getFS().create(new Path("/log4j.properties"));

        //1.3 对接流
        IOUtils.copyBytes(fis, fdo, 4096, true);
    }
}
