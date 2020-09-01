package com.wrx.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;

/**
 * 获取HDFS文件系统
 * 上传文件
 * 下载文件
 * 创建目录
 * 删除文件
 * 文件名更改
 * 查看文件详情
 * 类型判断
 * @author 19425
 */
public class HdfsTest1 {

    public static void main(String[] args) throws Exception {

        // 1、创建配置文件对象
        Configuration configuration =new  Configuration();
        /*
        这个对象可以在程序运行时临时修改Hadoop的一些配置参数
        系统中配置的副本数为“2”，这里设置为“1”
        因为存在 优先级  代码中  > resource的配置文件  > 集群  优先级
        因此最后的文件副本数为“1”。
        */
        configuration.set("dfs.replication","1");

        // 2、通过FileSystem创建  HDFS文件系统对象
        FileSystem fileSystem = FileSystem.get(
                URI.create("hdfs://192.168.111.117:9000"),
                configuration,
                "root");

        // 3、上传文件：调用上传文件方法
        uploads(fileSystem);

        // 4、下载文件：调用下载文件方法
//        downloads(fileSystem);

        // 5、创建文件
        //fileSystem.mkdirs(new Path("/goodTime.txt"));

        // 6、删除文件
//        fileSystem.delete(new Path("/goodTime.txt"),true);

        // 7、更改文件名
//        fileSystem.rename(
//                new Path("/goodTime.txt"),
//                new Path("/good.txt")
//        );

        // 8、查看文件详情
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path("/"));

        for (FileStatus fileStatus : fileStatuses) {
            //TODO  按照Hadoop UI的格式展示文件
            System.out.println(fileStatus.getPermission());
//                    "Permission"+fileStatus.getPermission())+
//                    "Owner"+fileStatus.getOwner()+
//                    "Group"+fileStatus.getGroup()+
//                    "Size"+fileStatus.getBlockSize()+
//                    "Last Modified"+fileStatus.getModificationTime()+
//                    "Replication"+fileStatus.getReplication()+
//                    "Block Size"+fileStatus.getBlockSize());
            // 类型判断
            boolean isFile = fileStatus.isFile();
            boolean isDirectory = fileStatus.isDirectory();
        }
    }

    /**
     * 文件上传方法
     * 参数为文件系统对象
     * @param fileSystem
     */
    public static void uploads(FileSystem fileSystem)throws Exception{
            fileSystem.copyFromLocalFile(
                    new Path("C:\\Users\\19425\\Desktop\\Lemon.txt"),
                    new Path("/"));
    }

    public static void downloads(FileSystem fileSystem) throws IOException {
        fileSystem.copyToLocalFile(false,
                new Path("/Lemon.txt"),
                new Path("C:\\Users\\19425\\Desktop\\Lemon"),
                true
        );
    }

}
