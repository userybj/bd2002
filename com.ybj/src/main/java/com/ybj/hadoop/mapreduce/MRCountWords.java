package com.ybj.hadoop.mapreduce;

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

public class MRCountWords {
    public static void main(String[] args) throws Exception {
        //1.获取job实例
        Job job = Job.getInstance(new Configuration());
        //2.设置job的运行主类
        job.setJarByClass(MRCountWords.class);
        //3.设置map的类
        job.setMapperClass(WordsMap.class);
        //4.设置reduce的类
        job.setReducerClass(WordsReduce.class);
        //5.设置map输出的类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);
        //6.设置reduce的输入类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        //7.设置job的输入路径
        FileInputFormat.setInputPaths(job,new Path(args[0]));
        //8.设置job的输出路径
        FileOutputFormat.setOutputPath(job,new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
    //map的静态内部类
    /**
     * MR读取文件时，keyin是LongWritable(当前行在文件中的字节偏移量 offset)；
     * valuein是Text(文本中的每一行字符串);
     * keyout是Text(存储每个单词);
     * valueout是LongWritable(存储单词出现的数量);
     */
    static class WordsMap extends Mapper<LongWritable,Text,Text,LongWritable>{
        private Text k = new Text();
        private LongWritable v = new LongWritable(1);
        //重写map方法
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //读取每行的字符串
            String line = value.toString();
            //拆分成单词
            String[] words = line.split("\\s+");
            //得到的单词进行处理
            for (String word : words) {
                word = word.replaceAll("\\W","").toLowerCase();
                if(!"".equals(word)){
                    k.set(word);
                    //用Context将处理完的结果写出到框架
                    context.write(k,v);
                }
            }
        }
    }
    //reduce的静态内部类
    /**
     * MR框架会将map的数据按照k进行分组；
     * 在调用Reducer里的reduce()方法时每次传入一个k和这个k对应的所有v
     */
    static class WordsReduce extends Reducer<Text,LongWritable,Text,LongWritable>{
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long count = 0L;
            for (LongWritable value : values) {
                count ++;
            }
            context.write(key,new LongWritable(count));
        }
    }
}
