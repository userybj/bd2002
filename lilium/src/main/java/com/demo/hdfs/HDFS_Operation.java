package com.demo.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class HDFS_Operation {

    private static FileSystem fs = null;

    @Test
    public void HDFSTest() throws IOException, InterruptedException {
        Configuration cf = new Configuration();
        cf.set("dfs.replication","2");

        fs = FileSystem.get(
                URI.create("hdfs://master:9000/"), cf, "root");
        fs.mkdirs(new Path("/pic"));
        copyToCluster("F:\\桌面\\西琳小恶魔.png","/pic");
    }

    private void copyToCluster(String srcStr,String dirStr){
        try {
            fs.copyFromLocalFile(false,false,
                    new Path(srcStr),
                    new Path(dirStr));
        } catch (IOException e) {
            if(e.getMessage().contains("exist")) {
                System.out.println("文件已存在！");
            }else{
                System.out.println("传输出错！");
            }

        }
    }

}
