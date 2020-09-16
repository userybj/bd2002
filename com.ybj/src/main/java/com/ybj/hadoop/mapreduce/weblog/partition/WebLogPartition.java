package com.ybj.hadoop.mapreduce.weblog.partition;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;

import java.io.IOException;

public class WebLogPartition {
    public static void main(String[] args) throws Exception{
        //1. 获取job实例
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        //2. 设置job运行的主类
        job.setJarByClass(WebLogPartition.class);

        //3. 设置Mapper的类
        job.setMapperClass(WLPMapper.class);

        //4. 设置Reducer的类
        job.setReducerClass(WLPReducer.class);

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


        // 11. 设置reducer数量
        job.setNumReduceTasks(10);

        //12.设置分区器
        //默认的分区器（按照reduce的数量去分区）
        //job.setPartitionerClass(HashPartitioner.class);
        //自定义的分区器
        job.setPartitionerClass(IpPartitioner.class);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
    //map类
    public static class WLPMapper extends Mapper< LongWritable, Text, Text, LongWritable>{
        private Text k = new Text();
        private LongWritable v = new LongWritable(1);
        @Override
        protected void map(LongWritable key,Text  value, Context context) throws IOException, InterruptedException {
            String ip = value.toString().split("\\s+")[0];
            k.set(ip);
            context.write(k,v);
        }
    }
    //reduce类
    public static class WLPReducer extends Reducer<Text,LongWritable,Text,LongWritable>{
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long count = 0;
            for (LongWritable value : values) {
                count++;
            }
            context.write(key,new LongWritable(count));
        }
    }
    //自定义分区器类(根据IP开头去去分)
    public static class IpPartitioner extends Partitioner<Text,LongWritable>{
        @Override
        public int getPartition(Text text, LongWritable longWritable, int i) {
            //根据当前传入的kv值进行计算得到分区编号
            return Integer.parseInt(text.toString().substring(0,1))-1;
        }
    }
}
