package com.ybj;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;


public class HDFSdemo01 {
    public static void main(String[] args) {
        try {
            // 1. 通过FileSystem创建  HDFS文件系统对象
            FileSystem fls = FileSystem.get(URI.create("hdfs://192.168.19.66:9000"), new Configuration(), "root");
        // 2. 上传文件 (覆盖)
            /*fls.copyFromLocalFile(new Path("C:\\Users\\ASUS\\Desktop\\spring.docx"),new Path("/"));
            fls.copyFromLocalFile(new Path("F:\\尚硅谷java课件\\html&js\\08-jsp"),new Path("/"));*/
        // 3.下载文件
            //fls.copyToLocalFile(new Path("/08-jsp/02-尚硅谷-jsp-jsp的小结.avi"),new Path("C:\\Users\\ASUS\\Desktop"));
        // 4.创建目录
            //fls.mkdirs(new Path("/first"));
        // 5.删除文件
            //fls.delete(new Path("/08-jsp/02-尚硅谷-jsp-jsp的小结.avi"),true);
        // 6.文件名更改
            //fls.rename(new Path("/08-jsp"),new Path("/jsp_data"));
        // 7.查看文件详情
            FileStatus[] files = fls.listStatus(new Path("/"));
            System.out.println("Permission\tOwner\tGroup\tSize\tLast Modified\tReplication\tBlock Size\t\tName");
            for(FileStatus fileStatus :files){
                System.out.println(fileStatus.getPermission()+"\t"+fileStatus.getOwner()+"\t"+fileStatus.getGroup()+"\t"+fileStatus.getLen()+"\t"+fileStatus.getModificationTime()+"\t"+fileStatus.getReplication()+"\t\t\t"+fileStatus.getBlockSize()+"\t"+fileStatus.getPath());
                // 8.类型判断
                boolean flag1 = fileStatus.isDirectory();
                boolean flag2 = fileStatus.isFile();
                System.out.println("**"+flag1+"**");
                System.out.println("&&"+flag2+"&&");
            }
            //hdfs的流操作
            //1. 下载文件
            //  HDFS  ->  java程序内存  ->  本地文件
            // 1.1 从HDFS开启输入流
            /*FSDataInputStream fis = fls.open(new Path("/spring.docx"));
            //1.2 获取输出流
            FileOutputStream fio = new FileOutputStream(new File("C:\\Users\\ASUS\\Desktop/spring.docx"));
            //1.3 复制流
            IOUtils.copyBytes(fis,fio,1024);*/
            //2. 上传文件
            //  本地文件  ->  java程序内存  ->  HDFS
            // 2.1 获取输入流
            /*FileInputStream fiss = new FileInputStream(new File("C:\\Users\\ASUS\\Desktop/spring.docx"));
            // 2.2 从HDFS获取输出流
            FSDataOutputStream fso = fls.create(new Path("/main/spring.docx"));
            // 2.3 复制流
            IOUtils.copyBytes(fiss,fso,4096,true);*/
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
