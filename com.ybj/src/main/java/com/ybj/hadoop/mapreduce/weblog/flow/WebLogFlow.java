package com.ybj.hadoop.mapreduce.weblog.flow;

import com.ybj.hadoop.mapreduce.weblog.entity.WebLog;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class WebLogFlow {
    public static void main(String[] args) throws Exception{
        //1. 获取job实例
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        //2. 设置job运行的主类
        job.setJarByClass(WebLogFlow.class);

        //3. 设置Mapper的类

        job.setMapperClass(FlowMapper.class);

        //4. 设置Reducer的类
        job.setReducerClass(FlowReducer.class);

        //5. 设置Mapper输出的类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(WebLog.class);

        //6. 设置Reducer输出的类型
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(WebLog.class);

        //7. 设置job的输入和输出路径
        FileInputFormat.setInputPaths(job,new Path("C:\\Users\\ASUS\\Desktop\\log\\demo1"));
        FileOutputFormat.setOutputPath(job,new Path("C:\\Users\\ASUS\\Desktop\\log\\demo5"));

        //8. 设置MR的输入格式
        job.setInputFormatClass(SequenceFileInputFormat.class);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
    //map类
    public static class FlowMapper extends Mapper<Text,Text,Text,WebLog>{
        private Text k = new Text();
        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            String[] lines = value.toString().split("\\n");
            for (String line : lines) {
                String[] strs = line.split("\\s+");
                String ip = strs[0];
                String time = null;
                Long status = 0L;
                String type = "";
                /*Long up = 0L;
                Long down = 0L;
                Long sum = 0L;*/
                try{
                    time = strs[3].replaceAll("\\[","").trim();
                    status = Long.parseLong(strs[8]);
                    type = strs[12].replaceAll("\\(","").trim();
                    /*up = Long.parseLong(strs[9]);
                    down = Long.parseLong(strs[strs.length-1]);
                    sum = up + down;*/
                }catch (Exception e){

                }
                WebLog webLog = new WebLog(ip,time,status,type);
                /*webLog.setType(type);
                webLog.setTime(time);
                webLog.setStatus(status);
                webLog.setIp(ip);*/
                k.set(ip);
                context.write(k,webLog);
            }
        }
    }
    //reduce类
    public static class FlowReducer extends Reducer<Text,WebLog, NullWritable,WebLog>{
        /*long sumUp = 0;
        long sumDown = 0;
        long sumSum = 0;*/
        WebLog v = new WebLog();
        @Override
        protected void reduce(Text key, Iterable<WebLog> values, Context context) throws IOException, InterruptedException {
            for (WebLog value : values) {
                v.setIp(value.getIp());
                v.setStatus(value.getStatus());
                v.setTime(value.getTime());
                v.setType(value.getType());
                /*sumUp += value.getUp();
                sumDown += value.getDown();
                sumSum += value.getSum();*/
                context.write(NullWritable.get(),v);
            }
            //WebLog v = new WebLog(key.toString(),sumUp,sumDown,sumSum);
        }
    }
}
