package com.xahj.utils;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.slf4j.impl.Log4jLoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * Author: Amos
 * E-mail: amos@amoscloud.com
 * Date: 2020/8/31
 * Time: 15:19
 * Description:
 */
public class HDFSUtils {
    private static InternalLogger logger = Log4JLoggerFactory.getInstance(HDFSUtils.class.getName());
    private static Properties prop = new Properties();
    private static FileSystem fs = null;

    static {
        try {
            prop.load(new FileReader(new File("C:\\Users\\Administrator\\Desktop\\bd2002\\amos\\src\\main\\resources\\my.properties")));
        } catch (IOException e) {
            logger.error("加载配置文件异常", e);
        }
    }

    public static FileSystem getFS() {

        try {
            Configuration conf = new Configuration();
            if (fs == null) {
                fs = FileSystem.get(
                        URI.create(prop.getProperty("hdfs.uri")),
                        conf,
                        prop.getProperty("hdfs.user")
                );
            }

        } catch (IOException e) {
            logger.error("获取文件系统出错！", e);
        } catch (InterruptedException e) {
            logger.error("获取文件系统出错！", e);
        }
        return fs;
    }
}
