package com.ybj.hadoop.mapreduce.weblog.partition;

import com.ybj.hadoop.mapreduce.weblog.flow.RiskVisit;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.HashMap;

public class MapperJoinTest {
    public static void main(String[] args) throws Exception{
        //1. 获取job实例
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        //2. 设置job运行的主类
        job.setJarByClass(MapperJoinTest.class);

        //3. 设置Mapper的类
        job.setMapperClass(MJMapper.class);

        //4.设置输出数据类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        //5.设置MR的输入和输出路径
        FileInputFormat.setInputPaths(job,new Path("C:\\Users\\ASUS\\Desktop\\student\\student.csv"));
        FileOutputFormat.setOutputPath(job,new Path("C:\\Users\\ASUS\\Desktop\\student\\output1"));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    static class MJMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
        private HashMap<String, String> map = new HashMap<>();
        private Text k = new Text();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            //把较小的表放在Map对象中
            FileSystem fs = FileSystem.get(context.getConfiguration());
            Path path = new Path("C:\\Users\\ASUS\\Desktop\\student\\grade.csv");
            FSDataInputStream open = fs.open(path);
            byte[] buf = new byte[(int) fs.getFileStatus(path).getLen()];
            open.read(buf);
            String grade = new String(buf);
            for (String line : grade.split("\n")) {
                String[] split = line.split("\t");
                map.put(split[0], line.substring(line.indexOf('\t')));
            }
            open.close();
        }
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // student表的数据
            String valueStr = value.toString();
            String[] split = valueStr.split("\t");
            if (split.length != 0 && !valueStr.isEmpty()) {
                String gradeId = split[4];
                if (map.containsKey(gradeId)) {
                    k.set(valueStr + map.get(gradeId));
                    context.write(k, NullWritable.get());
                }
            }
        }
        //清空内存中map的缓存释放内存
        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            map = null;
        }
    }
}
