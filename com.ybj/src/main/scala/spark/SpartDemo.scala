package spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SpartDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    // local[2]   使用本地模式运行spark  数字代表可用核心数
    conf.setMaster("local[2]")
    conf.setAppName("demo")
    val sc = new SparkContext(conf)
    //rdd是一个集合
    //textFile读文件 把每一行读取成一个String元素
    val rdd1:RDD[String] = sc.textFile("C:\\Users\\ASUS\\Desktop\\a.txt")
    val rdd2:RDD[String] = rdd1.flatMap(_.split("\\s+"))
    val rdd3:RDD[(String,Int)] = rdd2.map((_,1))
    val rdd4:RDD[(String,Int)] = rdd3.reduceByKey(_+_)
    rdd4.foreach(println)
    //sc.textFile("C:\\Users\\ASUS\\Desktop\\a.docx").flatMap(_.split("\\s+")).map((_,1)).reduceByKey(_+_).foreach(println)
    //获取分区数 默认分区数为设置的核心数
    //val partitions = rdd1.getNumPartitions
    //重新设置RDD的分区
    //rdd1.repartition(10)
  }
}
