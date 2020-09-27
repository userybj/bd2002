package com.ybj.hadoop.mapreduce.weblog.flow;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.omg.IOP.ExceptionDetailMessage;

import java.io.IOException;

public class StatisticalDistribution {
    public static void main(String[] args) throws Exception{
        //1. 获取job实例
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        //2. 设置job运行的主类
        job.setJarByClass(StatisticalDistribution.class);

        //3. 设置Mapper的类
        job.setMapperClass(SDMapper.class);

        //4. 设置Reducer的类
        job.setReducerClass(SDReducer.class);

        //5. 设置Mapper输出的类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        //6. 设置Reducer输出的类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        //7. 设置job的输入和输出路径
        FileInputFormat.setInputPaths(job,new Path("C:\\Users\\ASUS\\Desktop\\log\\demo6"));
        FileOutputFormat.setOutputPath(job,new Path("C:\\Users\\ASUS\\Desktop\\log\\demo8"));

        //8. 设置MR的输入格式
        //job.setInputFormatClass(SequenceFileInputFormat.class);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
    //map类
    public static class SDMapper extends Mapper<LongWritable, Text,Text,Text>{
        private Text k = new Text();
        private Text v = new Text();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] line = value.toString().split("\\t");
            String ip = line[0];
            String str1 = line[2];
            String str2 = line[4];
            k.set(ip);
            v.set(str1+"\t"+str2);
            context.write(k,v);
        }
    }
    //reduce类
    public static class SDReducer extends Reducer<Text,Text,Text, NullWritable>{
        private Text k = new Text();
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            Double num = 0D;
            Long num3 = 0L;
            Long num2 = 0L;
            for (Text value : values) {
                String[] split = value.toString().split("\\t");
                if(split[0].equals("404")){
                    num2 = Long.parseLong(split[1]);
                }
                try {
                    num3 = Long.parseLong(split[1]);
                    num3++;
                }catch (Exception e){}
                num = Double.valueOf(num2/num3);
            }
            k.set(key+"\t"+num);
            context.write(k,NullWritable.get());
        }
    }
}
