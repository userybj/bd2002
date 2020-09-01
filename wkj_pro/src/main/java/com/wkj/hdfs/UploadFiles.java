package com.wkj.hdfs;

import com.wkj.utils.HDFSUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;

import java.io.File;
import java.io.IOException;

/**
 * 上传、下载多个文件
 */
public class UploadFiles {
    public static void main(String[] args) throws IOException {
        //TODO  多文件上传
        //获得文件系统对象
        FileSystem fs = HDFSUtils.getFs();
        //创建HDFS文件夹
        fs.mkdirs(new Path("/files"));
        //创建文件路径数组
        Path[] paths = null;
        //获得本地文件集，并转化成数组
        File file = new File("F:\\学习资料\\学习笔记");
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            paths = new Path[listFiles.length];
            for (int i = 0; i < listFiles.length; i++) {
                paths[i] = new Path(listFiles[i].getCanonicalPath());
            }
            //进行文件上传，并关闭资源
            fs.copyFromLocalFile(false,true,paths,new Path("/files"));
        }


        //TODO 多文件下载

        fs.close();





    }
}
