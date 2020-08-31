package com.xahj.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Author: Amos
 * E-mail: amos@amoscloud.com
 * Date: 2020/8/31
 * Time: 13:39
 * Description:
 */
public class HDFSDemo1 {

    public static void main(String[] args) throws Exception {
        boolean flag;
        // HDFS 的操作


        // HDFS 的API入口类是FileSystem
        // FileSystem 有多个实现类
        // FileSystem 可以根据URI兼容各种各样不同的文件系统

        // 0. 创建配置文件对象

        // 这个对象可以在程序运行时临时修改Hadoop的一些配置参数
        // 优先级  代码中  > resource的配置文件  > 集群
        Configuration conf = new Configuration();
        conf.set("dfs.replication", "1");

        // 1. 通过FileSystem创建  HDFS文件系统对象

        FileSystem fs = FileSystem.get(
                URI.create("hdfs://bd0201:9000/"),
                conf,
                "root"
        );

        upload(fs);

        // 3. 下载文件
        download(fs);

        // 4. 创建目录
//         flag = fs.mkdirs(new Path("/2020-8-31"));
//        System.out.println(flag);

        // 5. 删除文件
//         flag = fs.delete(new Path("/2020-8-31"),true);
//        System.out.println(flag);

        // 6. 文件名更改
        flag = fs.rename(new Path("/1.txt"), new Path("/2.txt"));
        System.out.println(flag);

        // 7. 查看文件详情
        FileStatus[] fileStatuses = fs.listStatus(new Path("/"));
        for (FileStatus fileStatus : fileStatuses) {
            //todo  按照Hadoop UI的格式展示文件

        }
        // 8. 类型判断


    }

    private static void download(FileSystem fs) throws IOException {
        fs.copyToLocalFile(
                new Path("/1.txt"),
                new Path("C:\\Users\\Administrator\\Desktop\\1.txt")
        );
    }

    private static void upload(FileSystem fs) throws IOException {
        // 2. 上传文件 (覆盖)
        fs.copyFromLocalFile(
                new Path("C:\\Users\\Administrator\\Desktop\\profile"),
                new Path("/")
        );
    }
}
