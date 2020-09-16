package com.ybj.hadoop.mapreduce.weblog;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class WebLogCombineCount {
    public static void main(String[] args) throws Exception {
        //1. 获取job实例
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        //2. 设置job运行的主类
        job.setJarByClass(WebLogCombineCount.class);

        //3. 设置Mapper的类
        job.setMapperClass(WLMapper.class);

        //4. 设置Reducer的类
        job.setReducerClass(WLReducer.class);

        //5. 设置Mapper输出的类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        //6. 设置Reducer输出的类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);


        //7. 设置job的输入路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        //8. 设置job的输出路径
        FileOutputFormat.setOutputPath(job, new Path(args[1]));


        //9. 设置MR的输入格式
        // 默认使用的TextInputFormat
        job.setInputFormatClass(CombineTextInputFormat.class);

        //10. 设置CombineTextInputFormat的分片大小
        CombineTextInputFormat.setMinInputSplitSize(job,20971520);
        CombineTextInputFormat.setMaxInputSplitSize(job,20971520);


        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
    //map内部类
    public static class WLMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        private Text k = new Text();
        private LongWritable v = new LongWritable(1);
        @Override
        protected void map(LongWritable key, Text value, Context context) throws InterruptedException, IOException {
            String ip = value.toString().split("\\s+")[0];
            //String ip = value.toString().split("\\s+")[0];
            k.set(ip);
            context.write(k, v);
        }
    }
    //reduce内部类
    public static class WLReducer extends Reducer< Text, LongWritable, Text, LongWritable> {
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long count = 0;
            for (LongWritable value : values) {
                count++;
            }
            context.write(key,new LongWritable(count));
        }
    }
}
