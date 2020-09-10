package com.demo.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class HDFS_Operation {
    private static Logger logger = Logger.getLogger(HDFS_Operation.class);
    private static FileSystem fs = null;

//    static{
//        try {
//            //建议采用绝对地址。
//            System.load("H:\\hadoop\\hadoop-2.7.7\\bin\\hadoop.dll");
//        } catch (UnsatisfiedLinkError e) {
//            System.err.println("Native code library failed to load.\n" + e);
//            System.exit(1);
//        }
//    }

    @Test
    public void HDFSTest() throws IOException, InterruptedException {
        //1.创建配置文件
        Configuration cf = new Configuration();
        //1.1设置配置文件
        cf.set("dfs.replication","2");
        //2.获取hdfs文件系统对象
        fs = FileSystem.get(
                URI.create("hdfs://192.168.110.10:9000/"), cf, "root");
        //3.获取文件列表
        RemoteIterator<LocatedFileStatus> files = fs.listFiles(new Path("/"), true);
        while(files.hasNext()){
            System.out.println(files.next().toString());
        }
        //copyToCluster("F:\\桌面\\kibana-7.5.2-linux-x86_64.tar.gz","/");
        //copyFromCluster("/hadoop-2.7.7.tar.gz","F:\\桌面\\hadoop");
    }

    private void copyToCluster(String srcStr,String desStr){
        try {
            fs.copyFromLocalFile(false,false,
                    new Path(srcStr),
                    new Path(desStr));
            System.out.println(
                    "传输成功！"
            );
        } catch (IOException e) {
            if(e.getMessage().contains("exist")) {
                System.out.println("文件已存在！");
            }else{
                System.out.println("传输出错！");
            }
        }
    }

    private void copyFromCluster(String localPath ,String dfsPath){
        try{
            fs.copyToLocalFile(false,new Path(localPath),
                    new Path(dfsPath));
            logger.debug("下载成功！");
        } catch (IOException e) {
            logger.debug("下载失败！");
            e.printStackTrace();
        }
    }

}
