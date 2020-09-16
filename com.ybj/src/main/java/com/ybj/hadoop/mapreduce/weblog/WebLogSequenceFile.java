package com.ybj.hadoop.mapreduce.weblog;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import java.io.IOException;

public class WebLogSequenceFile {
    public static void main(String[] args) throws Exception{
        //1.获取job实例
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        //2.设置job主类
        job.setJarByClass(WebLogSequenceFile.class);
        //3.设置map类
        job.setMapperClass(WLSMapper.class);
        //4.设置输出类
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        //5.设置job的输入和输出路径
        FileInputFormat.setInputPaths(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));
        //6.设置MR的输入格式
        job.setInputFormatClass(MyFileInputFormat.class);
        //7.设置MR的输出格式
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
    //map类
    public static class WLSMapper extends Mapper<Text,Text,Text,Text>{
        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            context.write(key,value);
        }
    }
    //自定义文件读取类
    public static class MyFileInputFormat extends FileInputFormat<Text,Text>{
        @Override
        public RecordReader<Text, Text> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
            MyRecordReader reader = new MyRecordReader();
            reader.initialize(split,context);
            return reader;
        }
    }
    /**
     *  这个类用来做IO操作读取数据，并将数据发送给Mapper进行处理
     */
    public static class MyRecordReader extends RecordReader<Text,Text>{
        private InputSplit split = null;
        private TaskAttemptContext context = null;
        private Text k = new Text();
        private Text v = new Text();
        private boolean flag = true;
        @Override
        //初始化方法
        public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
            this.split = split;
            this.context = context;
        }

        @Override
        //读取文件，并判断文件是否完整
        public boolean nextKeyValue() throws IOException, InterruptedException {
            //第一次进来返回true，第二次进来返回false
            if(flag){
                //1.从context中获取配置对象，获取文件系统
                FileSystem fs = FileSystem.get(context.getConfiguration());
                //2.从split中获取切片信息，获取文件的路径
                Path path = ((FileSplit) split).getPath();
                long length = split.getLength();
                //3.打开输入流，根据path读文件
                FSDataInputStream open = fs.open(path);
                //4.读取整个文件
                byte [] buf = new byte[(int)length];
                int read = open.read(buf);
                if(read != 0) {
                    //5.将文件名设置为k
                    k.set(path.getName());
                    //6.将文件内容设置为v
                    v.set(new String(buf));
                }
                open.close();
                flag = false;
                return true;
            }
            return flag;
        }

        @Override
        public Text getCurrentKey() throws IOException, InterruptedException {
            return k;
        }

        @Override
        public Text getCurrentValue() throws IOException, InterruptedException {
            return v;
        }

        @Override
        public float getProgress() throws IOException, InterruptedException {
            return 0;
        }

        @Override
        public void close() throws IOException {

        }
    }
}
