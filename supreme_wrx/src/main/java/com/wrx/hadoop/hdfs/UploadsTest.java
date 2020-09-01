package com.wrx.hadoop.hdfs;

import com.wrx.util.HdfsUtil;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.IOException;

/**
 * 上传多个文件
 * @author 19425
 */
public class UploadsTest {

    public static void main(String[] args) throws Exception {

        // 获得文件输入流
        FileSystem fileSystem = HdfsUtil.getFileSystem();
        //  在虚拟机上创建一个文件夹
        fileSystem.mkdirs(new Path("/CentOS命令报错"));

        //  文件夹地址数组
        Path[] paths = null;

        // 声明文件夹位置
        File file = new File("C:\\Users\\19425\\Desktop\\CentOS命令报错");
        // 将文件夹的位置打包成数组
        File[] files = file.listFiles();
        if (files == null){
             paths = new Path[files.length];
            for (int i = 0; i < paths.length; i++) {
                paths[i] = new Path(files[i].getCanonicalPath());
            }
            //  将便利出来的地址依次上传
            fileSystem.copyFromLocalFile(false,true,paths,new Path("/CentOS命令报错"));
        }
        //  关闭通道
        fileSystem.close();
    }

}
