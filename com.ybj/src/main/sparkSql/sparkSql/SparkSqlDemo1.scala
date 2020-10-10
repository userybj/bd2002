package sparkSql

import java.text.SimpleDateFormat
import java.util.Locale

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object SparkSqlDemo1 {
  def main(args: Array[String]): Unit = {
    //1.获取SparkSQL的程序入口
    val spark = SparkSession.builder()
      .appName("sparkSqlDemo1")
      .master("local[2]")
      .getOrCreate()
    import spark.implicits._
    val sc = spark.sparkContext
    //2. 创建df实例
    // sparkSql中使用DataFrame 和Dataset两种数据抽象
    // 当然，SparkSql中也可以使用RDD

    //2.1 日志处理案例
    val rdd:RDD[String] = sc.textFile("C:\\Users\\ASUS\\Desktop\\log\\logs\\access.log-20190924")
    val rdd1:RDD[(String,String,String,String)]=rdd.filter(line=>line.split("\\s+").length > 14)
      .map(line =>{
      val strs = line.split("\\s+")
      val ip = strs(0)
      val time = strs(3).tail
      val rCode = strs(8)
      val types = strs(12).tail
      (ip,time,rCode,types)
    })
    //rdd转df
    rdd1.toDF()
    //    .show()

    // rdd的分组计数
    rdd1.map(x=>(x._1,1))
      .reduceByKey(_+_)

    val df1:DataFrame = rdd1.toDF("ip","time","rCode","types")
    //  .show()

    // df或者ds风格(sql风格)
    // 1.创建临时视图(给df起一个表名)
    df1.createOrReplaceTempView("log1")
    // 2.做sql运算
    spark.sql(
      """
        |select * from
        |(select ip,count(*) as count
        |from log1
        |group by ip)
        |as log2
        |order by log2.count desc
        |""".stripMargin)
    //  .show()

      //自定义函数
    spark.udf.register("format_time",(date:String)=>{
      val parser = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.US)
      val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      formatter.format(parser.parse(date))
    })

    spark.sql("select ip ,format_time(time) from log1")
    //  .show()

    //rdd1转为泛型为WebLog
    val rdd2:RDD[WebLog] = rdd1.map(x=>WebLog(x._1,x._2,x._3,x._4))
    //rdd转ds
    val ds1:Dataset[WebLog] = rdd2.toDS()
    //ds1.show()
    //df转ds
    val ds2:Dataset[WebLog] = df1.as[WebLog]
    ds2.show()
  }
}
//样例类
case class WebLog(ip: String, time: String, rCode: String, types: String)
