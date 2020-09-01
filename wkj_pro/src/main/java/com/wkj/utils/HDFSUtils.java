package com.wkj.utils;


import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

public class HDFSUtils {

    private static InternalLogger logger = Log4JLoggerFactory.getInstance(HDFSUtils.class.getName());
    private static Properties prop = new Properties();
    private static FileSystem fs = null;

    //获取配置文件内容
    static {
        try {
            prop.load(new FileReader(new File("F:\\idea\\bd2002\\wkj_pro\\src\\main\\resources\\hdfs-configure.properties")));
        } catch (IOException e) {
            logger.error("加载配置文件异常", e);
        }
    }

    //获得FileSystem对象
    public static FileSystem getFs() {
        if (fs == null) {
            try {
                fs = FileSystem.get(URI.create(prop.getProperty("hdfs.uri")), new Configuration(), prop.getProperty("hdfs.user"));
            } catch (IOException e) {
                logger.error("输入输出异常", e);
            } catch (InterruptedException e) {
                logger.error("数据传输中断", e);
            }
        }

        return fs;
    }
}
