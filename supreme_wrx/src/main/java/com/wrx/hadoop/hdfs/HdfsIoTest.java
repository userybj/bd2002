package com.wrx.hadoop.hdfs;

import com.wrx.util.HdfsUtil;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.*;

/**
 * HDFS的IO操作
 * @author 19425
 */
public class HdfsIoTest {

    public static void main(String[] args) throws Exception {

        // 1、文件上传：调用文件上传方法
        uploads();

        // 2、文件下载：调用文件下载方法
        downloads();
    }

    /**
     * 文件上传方法
     * @throws
     */
    private static void uploads() throws IOException {

        //  本地文件  ->  java程序内存  ->  HDFS
        //  获取输入流
        FileInputStream fileInputStream= new FileInputStream(new File("C:\\Users\\19425\\Desktop\\lyric.txt"));

        //  获取通向HDFS的输出流
        FSDataOutputStream stream = HdfsUtil.getFileSystem().create(new Path("/lyric.txt"));

        //  对接流
        IOUtils.copyBytes(fileInputStream,stream,4096,true);
    }

    private static void downloads() throws IOException {

        //  HDFS  ->  java程序内存  ->  本地文件
        //  从HDFS开启输入流
        FSDataInputStream inputStream = HdfsUtil.getFileSystem().open(new Path("/lyric.txt"));

        //  获取输入流
        FileOutputStream fileOutputStream = new FileOutputStream(new File("C:\\Users\\19425\\Desktop\\Lemon\\lyric.txt"));

        //  对接流
        IOUtils.copyBytes(inputStream,fileOutputStream,4096,true);

    }

}
