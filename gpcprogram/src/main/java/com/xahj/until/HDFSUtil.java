package com.xahj.until;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

public class HDFSUtil {
    private static InternalLogger logger = Log4JLoggerFactory.getInstance(HDFSUtil.class.getName());
    private static Properties prop = new Properties();
    private static FileSystem fs = null;

    static {
        try {
            prop.load(new FileReader(new File("E:\\myproject\\bd2002\\gpcprogram\\src\\main\\resources\\my.properties")));
        } catch (IOException e) {
            logger.error("加载配置文件异常", e);
        }
    }

    public static FileSystem getFs(){
        Configuration conf = new Configuration();
        try {
            if (fs==null) {
                fs =FileSystem.get(URI.create(prop.getProperty("hdfs.uri")),
                        conf,prop.getProperty("hdfs.user"));
            }
        } catch (Exception e) {
           logger.error("文件系统出错", e);
        }
    return fs;
    }
}
