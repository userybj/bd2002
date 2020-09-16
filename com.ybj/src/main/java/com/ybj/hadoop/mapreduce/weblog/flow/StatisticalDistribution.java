package com.ybj.hadoop.mapreduce.weblog.flow;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class StatisticalDistribution {
    public static void main(String[] args) {

    }
    //map类
    public static class SDMapper extends Mapper<LongWritable, Text,Text,LongWritable>{

    }
}
