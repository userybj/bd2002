package com.ybj.hadoop.mapreduce.weblog.flow;

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

public class CountUse {
    public static void main(String[] args) throws Exception{
        //1. 获取job实例
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        //2. 设置job运行的主类
        job.setJarByClass(CountUse.class);

        //3. 设置Mapper的类

        job.setMapperClass(CUMapper.class);

        //4. 设置Reducer的类
        job.setReducerClass(CUReduce.class);

        //5. 设置Mapper输出的类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        //6. 设置Reducer输出的类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //7. 设置job的输入和输出路径
        FileInputFormat.setInputPaths(job,new Path("C:\\Users\\ASUS\\Desktop\\log\\demo5"));
        FileOutputFormat.setOutputPath(job,new Path("C:\\Users\\ASUS\\Desktop\\log\\demo7"));

        //8. 设置MR的输入格式
        //job.setInputFormatClass(SequenceFileInputFormat.class);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
    //map类
    public static class CUMapper extends Mapper<LongWritable, Text,Text,LongWritable>{
        private LongWritable v = new LongWritable(1);
        private Text k = new Text();
        String use = null;
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] line = value.toString().split("\\t");
            if(line.length > 3){
                use = line[3].replaceAll("\\W","").toLowerCase();
                k.set(use);
                context.write(k,v);
            }
        }
    }
    //reduce类
    public static class CUReduce extends Reducer<Text,LongWritable,Text,LongWritable>{
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            Long count = 0L;
            for (LongWritable value : values) {
                count ++;
            }
            context.write(key,new LongWritable(count));
        }
    }
}
