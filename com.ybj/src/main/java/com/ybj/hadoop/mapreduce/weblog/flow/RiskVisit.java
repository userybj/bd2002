package com.ybj.hadoop.mapreduce.weblog.flow;

import com.ybj.hadoop.mapreduce.weblog.entity.WebLog;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class RiskVisit {
    public static void main(String[] args) throws Exception{
        //1. 获取job实例
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        //2. 设置job运行的主类
        job.setJarByClass(RiskVisit.class);

        //3. 设置Mapper的类

        job.setMapperClass(RVMapper.class);

        //4. 设置Reducer的类
        job.setReducerClass(RVReducer.class);

        //5. 设置Mapper输出的类型
        job.setMapOutputKeyClass(WebLog.class);
        job.setMapOutputValueClass(LongWritable.class);

        //6. 设置Reducer输出的类型
        job.setOutputKeyClass(WebLog.class);
        job.setOutputValueClass(LongWritable.class);

        //7. 设置job的输入和输出路径
        FileInputFormat.setInputPaths(job,new Path("C:\\Users\\ASUS\\Desktop\\log\\demo5"));
        FileOutputFormat.setOutputPath(job,new Path("C:\\Users\\ASUS\\Desktop\\log\\demo6"));

        //8. 设置MR的输入格式
        //job.setInputFormatClass(SequenceFileInputFormat.class);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
    //map类
    public static class RVMapper extends Mapper<LongWritable,Text, WebLog, LongWritable>{
        private LongWritable v = new LongWritable(1);
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] line = value.toString().split("\\t");
                String ip = line[0];
                Long status = 0L;
                try{
                    status = Long.parseLong(line[2]);
                }catch (Exception e){}
                WebLog webLog = new WebLog();
                webLog.setIp(ip);
                webLog.setStatus(status);
                context.write(webLog,v);
        }
    }
    //reduce类
    public static class RVReducer extends Reducer<WebLog,LongWritable,WebLog,LongWritable>{
        @Override
        protected void reduce(WebLog key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            Long count = 0L;
            for (LongWritable value : values) {
                count ++;
            }
            context.write(key,new LongWritable(count));
        }
    }
}
