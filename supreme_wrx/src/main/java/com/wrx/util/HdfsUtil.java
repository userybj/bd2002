package com.wrx.util;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

/**
 * @author 19425
 */
public class HdfsUtil {

    private static InternalLogger logger = Log4JLoggerFactory.getInstance(HdfsUtil.class.getName());
    private static Properties properties = new Properties();
    private static FileSystem fileSystem = null;

    static {
        try {
            properties.load(new FileReader("E:\\班级项目\\WRX\\bd2002\\supreme_wrx\\src\\main\\resources\\my.properties"));
        } catch (IOException e) {
            logger.error("加载配置文件异常",e);
        }
    }

    public static FileSystem getFileSystem(){

        Configuration configuration = new Configuration();
        if (fileSystem == null){
            try {
                fileSystem = FileSystem.get(
                        URI.create(properties.getProperty("hdfs.uri")),
                        configuration,
                        properties.getProperty("hdfs.user")
                );
            } catch (IOException e) {
                logger.error("获取文件系统出错！", e);
            } catch (InterruptedException e) {
                logger.error("获取文件系统出错！", e);
            }
        }

        return fileSystem;
    }

}
