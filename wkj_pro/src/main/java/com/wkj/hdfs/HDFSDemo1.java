package com.wkj.hdfs;
/**
 * HDFS 的API入口类是FileSystem
 * FileSystem 有多个实现类
 * FileSystem 可以根据URI兼容各种各样不同的文件系统
 */

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;
import java.text.SimpleDateFormat;

public class HDFSDemo1 {
    public static void main(String[] args) throws Exception{
        /*// 0. 创建配置文件对象

        // 这个对象可以在程序运行时临时修改Hadoop的一些配置参数
        // 优先级  代码中  > resource的配置文件  > 集群
        Configuration conf = new Configuration();
        conf.set("dfs.replication", "1");*/
        // 1. 通过FileSystem创建  HDFS文件系统对象
        FileSystem fs = FileSystem.get(URI.create("hdfs://192.168.111.11:9000/"), new Configuration(), "root");
        System.out.println(fs);
        /*// 2.上传文件（覆盖已有文件）
        fs.copyFromLocalFile(new Path("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt"),new Path("/1.txt"));*/
        //3、下载文件
        fs.copyToLocalFile(new Path("/1.txt"),
                new Path("C:\\Users\\Administrator\\Desktop\\下载的文件.txt"));
        //4.创建目录
        boolean mkdirs = fs.mkdirs(new Path("/newdata"));
        System.out.println(mkdirs);
        //5.删除文件
        boolean delete = fs.delete(new Path("/2.txt"));
        System.out.println(delete);
        //6.文件名更改
        boolean rename = fs.rename(new Path("/3.txt"), new Path("/111.txt"));
        System.out.println(rename);
        //7.查看文件详情
        //通过路径获得多个文件，并存到数组中
        FileStatus[] fileStatuses = fs.listStatus(new Path("/"));
        //遍历数组元素
        for (FileStatus fileStatus : fileStatuses) {
            //判断元素是否为文件类型
            boolean file = fileStatus.isFile();
            //判断元素是否为文件夹类型
            boolean directory = fileStatus.isDirectory();
            System.out.println(fileStatus.getPermission()+"@@@"+fileStatus.getOwner()+"@@@"+fileStatus.getGroup()+"@@@"+fileStatus.getLen()+"@@@"+new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(fileStatus.getModificationTime())+"@@@"+fileStatus.getReplication()+"@@@"+fileStatus.getBlockSize()+"@@@"+file+"@@@"+directory);
        }
        //8.类型判断
    }
}
