package com.ybj.hadoop.mapreduce.weblog.partition;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReduceJoinTest {
    public static void main(String[] args) throws Exception{
        //1. 获取job实例
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        //2. 设置job运行的主类
        job.setJarByClass(ReduceJoinTest.class);

        //3. 设置Mapper的类

        job.setMapperClass(MyMapper.class);

        //4. 设置Reducer的类
        job.setReducerClass(MyReducer.class);

        //5. 设置Mapper输出的类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        //6. 设置Reducer输出的类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        //7. 设置job的输入和输出路径
        FileInputFormat.setInputPaths(job,new Path("C:\\Users\\ASUS\\Desktop\\student"));
        FileOutputFormat.setOutputPath(job,new Path("C:\\Users\\ASUS\\Desktop\\student\\output"));

        //8. 设置MR的输入格式
        //job.setInputFormatClass(SequenceFileInputFormat.class);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
    //map类
    public static class MyMapper extends Mapper<LongWritable, Text,Text,Text>{
        private Text k = new Text();
        private Text v = new Text();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //如果两个表字段差异很明确 可以直接根据长度或者字段特点进行区分
            String valueStr = value.toString();
            String[] line = valueStr.split("\\t");
            /*if(line.length != 0 && !valueStr.isEmpty()) {
                if (line.length <= 2) {
                    k.set(line[0]);
                    //不同的v经行标志，方便reduce操作
                    v.set("g|" + valueStr);
                } else {
                    k.set(line[4]);
                    v.set("s|" + valueStr);
                }
                context.write(k, v);
            }*/
            //如果表内容不好区分，可以通过文件名来区分
            //context拿到切片信息
            FileSplit fileSplit = (FileSplit)context.getInputSplit();
            //切片中拿到文件名
            String name = fileSplit.getPath().getName();
            //根据文件名判断
            if(line.length != 0 && !valueStr.isEmpty()){
                if (name.startsWith("grade")){
                    k.set(line[0]);
                    //不同的v经行标志，方便reduce操作
                    v.set("g|" + valueStr);
                }else {
                    k.set(line[4]);
                    v.set("s|" + valueStr);
                }
                context.write(k, v);
            }
        }
    }
    //reduce类
    //                                 join的键是年级ID
    //                                 同一个年纪 的多个学生回进入同一次reduce
    public static class MyReducer extends Reducer<Text,Text,Text, NullWritable>{
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            List<String> stu = new ArrayList<>();
            String grade = null;
            for (Text value : values) {
                String valueStr = value.toString();
                //找到标志
                String flag = valueStr.split("\\|")[0];
                //找到来自student表的数据
                if("s".equals(flag)){
                    stu.add(valueStr.substring(2));
                 //找到来自grade表的数据，去掉gradeID
                }else {
                    grade = valueStr.substring(valueStr.indexOf('\t'));
                }
            }
            //将两个数据拼接起来
            if(stu.size() != 0 && grade != null){
                for (String stus : stu) {
                    context.write(new Text(stus+grade),NullWritable.get());
                }
            }
        }
    }
}
