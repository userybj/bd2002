package com.xahj.hadoop.mapreduce.wordcount;


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

/**
 * Created with IntelliJ IDEA.
 * Author: Amos
 * E-mail: amos@amoscloud.com
 * Date: 2020/9/10
 * Time: 9:19
 * Description:
 */
public class MRWordCount {

    public static void main(String[] args) throws Exception {

        //1. 获取job实例
        Configuration conf = new Configuration();
//        conf.set("mapreduce.input.fileinputformat.split.minsize","12313123");
        Job job = Job.getInstance(conf);

        //2. 设置job运行的主类
        job.setJarByClass(MRWordCount.class);

        //3. 设置Mapper的类
        job.setMapperClass(WCMapper.class);

        //4. 设置Reducer的类
        job.setReducerClass(WCReducer.class);

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


        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    //                      mr读取文本文件时 keyIn是LongWritable类型
    //                              当前行在文件中的字节偏移量 offset
    //                                    valueIn是Text类型
    //                               文本中的每一行字符串
    //               keyOut Text 存储每个单词
    //               ValueOut LongWritable 存储单词出现的数量
    public static class WCMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        private Text k = new Text();
        private LongWritable v = new LongWritable(1);

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // 1. 取出每行的字符串
            String line = value.toString();
            // 2. 切分成单词
            String[] words = line.split("\\s+");
            // 3. 用context将处理完的结果写出到框架
            for (String word : words) {
                word = word.replaceAll("\\W", "").toLowerCase();
                if (!"".equals(word)) {
                    k.set(word);
                    context.write(k, v);
                }
            }
        }
    }

    public static class WCReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
        @Override
        //   MR框架会将Mapper输出的数据按照Key进行分组
        //   在调用Reducer的reduce方法时
        //   每次传入一个key 和这个key对应的所有Value
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long count = 0L;
            for (LongWritable value : values) {
                count++;
            }
            context.write(key, new LongWritable(count));
        }
    }
}
