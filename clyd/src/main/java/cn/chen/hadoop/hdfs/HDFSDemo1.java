package cn.chen.hadoop.hdfs;

import cn.chen.hadoop.utils.HDFSUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author:chenliuyiding
 * @date 2020/8/31 13: 39:
 * @description
 **/
public class HDFSDemo1 {
    private FileSystem fs;
    @Before
    public void init() throws Exception {
        // 上传文件
        FileSystem fs = HDFSUtils.connectFs();
    }
    @After
    public void close() throws IOException {
      HDFSUtils.close();
    }
    // hadoop fs(运行一个通用的用户客户端) -mkdir /xxx
    // 创建一个客户端对象,调用创建目录的方法，路径作为方法的参数
    @Test
    public void testMkdir() throws IOException, InterruptedException, URISyntaxException {
        //FileSystem fs = FileSystem.get(new URI("hdfs://hadoop101:9000"),conf,"chen");
        System.out.println(fs);
        fs.mkdirs(new Path("/idea1"));
    }
    // 上传文件：hadoop fs -put
    @Test
    public void testUpload()throws Exception{
        fs.copyFromLocalFile(false,true,new Path("E://a.txt"),new Path("/"));
    }
    @Test
    // 下载文件：hadoop fs -get
    public void testDownload()throws Exception{
        fs.copyToLocalFile(false,new Path("/wcinput"),new Path("e://"),false);
    }
    @Test
    // 删除文件：hadoop fs -rm -rf xxx
    public void testDelete()throws Exception{
        fs.delete(new Path("/idea"), true);
    }

    // 重命名：hadoop fs -mv 原名字 目标文件
    @Test
    public void testRename()throws Exception{
        fs.rename(new Path("/idea1"), new Path("/ideaDir"));
    }
    // 判断当前路径是否存在
    @Test
    public void testIfPathExist()throws Exception{
        System.out.println(fs.exists(new Path("/ideaBir")));
    }
    @Test
    // 判断当前路径是目录还是一个文件
    public void testFileOrDir()throws Exception{
        Path path = new Path("/wcinput");
       /* System.out.println(fs.isDirectory(path));
        System.out.println(fs.isFile(path));*/
        /*FileStatus fileStatus = fs.getFileStatus(path);
        System.out.println("是否是目录："+fileStatus.isDirectory());
        System.out.println("是否是文件："+fileStatus.isFile());*/
        FileStatus[] fileStatuses = fs.listStatus(path);
        for (FileStatus fileStatus : fileStatuses) {
            String name = fileStatus.getPath().getName();
            System.out.println(name);
            System.out.println("是否是目录："+fileStatus.isDirectory());
            System.out.println("是否是文件："+fileStatus.isFile());
        }
    }

}
