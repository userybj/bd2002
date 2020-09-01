package cn.chen.hadoop.utils;


import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;


import java.io.File;
import java.io.FileInputStream;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

/**
 * @author:chenliuyiding
 * @date 2020/8/31 19: 11:
 * @description
 **/
public class HDFSUtils {
    // 创建配置文件对象
    private static Properties prop = new Properties();
    // 创建日志对象
    private static InternalLogger logger = Log4JLoggerFactory.getInstance(HDFSUtils.class.getName());
    // 创建文件管理对象
    private static FileSystem fs = null;
    private static Configuration conf = new Configuration();
    static {
        try {
            prop.load(new FileReader(new File("E:\\IdeaProjects\\bd2002\\clyd\\src\\main\\resources\\clyd.properties")));
        } catch (IOException e) {
            logger.error("加载配置文件异常", e);
        }
    }

    public static FileSystem connectFs() {
        try {
            try {
                if (fs == null) {
                    fs = FileSystem.get(
                            URI.create(prop.getProperty("hdfs.uri")),
                            conf,
                            prop.getProperty("hdfs.user")
                    );
                }

            } catch (InterruptedException e) {
                logger.error("文件系统异常", e);
            }
        } catch (IOException e) {
            logger.error("文件上传异常", e);
        }
        return fs;
    }

    public static void close() {
        if (fs != null) {
            try {
                fs.close();
            } catch (IOException e) {
                logger.error("关闭异常", e);
            }
        }
    }
}
