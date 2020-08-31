package com.xahj.hadoop.hdfs;

import com.xahj.utils.HDFSUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * Author: Amos
 * E-mail: amos@amoscloud.com
 * Date: 2020/8/31
 * Time: 17:16
 * Description:
 */
public class UploadLogs {
    public static void main(String[] args) throws Exception {
        FileSystem fs = HDFSUtils.getFS();


        fs.mkdirs(new Path("/logs"));

        Path[] paths = null;

        File parent = new File("C:\\Users\\Administrator\\Desktop\\tmp\\logs");
        File[] children = parent.listFiles();
        if (children != null) {
            paths = new Path[children.length];
            for (int i = 0; i < children.length; i++) {
                paths[i] = new Path(children[i].getCanonicalPath());
            }
            fs.copyFromLocalFile(false, true, paths, new Path("/logs"));
        }
        fs.close();

    }
}
