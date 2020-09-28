package test

import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}
import utils.DateFormatter

object WebLogDemo {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf()
      .setAppName("weblog")
      .setMaster("local[4]"))

    //读取数据
    val rdd1:RDD[String] = sc.textFile("C:\\Users\\ASUS\\Desktop\\log\\logs\\access.log-20190924")


    //0.进行数据清洗
    rdd1.filter(filter1)
      .map(writeOut)
      //变身为((ip,rCode),num)
      //大量404和400
      .map(x => ((x._1,x._3),1))
      //根据相同(ip,rCode)聚合得到((ip,rCode),countrCode)
      .reduceByKey(_+_)
      //变身为((0,ip),countrCode)
      .map(x => {
        val num = x._1._2 match {
          case _ if x._1._2 == 400 || x._1._2 == 404 => 0
          case _ => 1
        }
        ((num,x._1._1),x._2)
      })
      //按照(0,ip)聚合
      .reduceByKey(_+_)
      //变身为(ip,(0,countrCode))
      .map(x => (x._1._2,(x._1._1,x._2)))
      //聚合为(ip,(0,num,total))
      .combineByKey(
        (v:(Int,Int)) => (v._1,v._2,v._2),
        (c:(Int,Int,Int),v:(Int,Int)) => (c._1,c._2,c._3+v._2),
        (c1:(Int,Int,Int),c2:(Int,Int,Int)) => (c1._1,c1._2,c1._3+c2._3)
      )
      //变身为(ip,probability,num,total)
      .map(x => {
        val num = x._2._1 match {
          case 0 => x._2._2*1.0/x._2._3
          case 1 => 1-(x._2._2*1.0/x._2._3)
        }
        val num1 = x._2._1 match {
          case 0 => x._2._2
          case 1 => x._2._3-x._2._2
        }
        (x._1,num,num1,x._2._3)
        })
      .repartition(1)
      .sortBy(_._2,false)
      //.foreach(println)


    //高频访问(同一时间访问超过7次)
    rdd1.filter(filter1)
      .map(writeOut)
      //变身为((ip,time),1)
      .map(x => ((x._1,x._2),1))
      .reduceByKey(_+_)
      .filter(_._2 >=7)
      .map(x => (x._1._1,1))
      .reduceByKey(_+_)
      .sortBy(_._1,false)
      .map(_._1)
      .repartition(1)
      //.foreach(println)

    //2.统计用户的地理位置分布情况
    rdd1.filter(filter1)
      .map(writeOut)
      .map(x => (x._1,1))
      .reduceByKey(_+_)
      //.foreach(println)

    //3.统计用户的设备类型占比
    rdd1.filter(filter1)
      .map(writeOut)
      .map(x =>(x._4,1))
      .reduceByKey(_+_)
      .map(x =>(x._1,x._2))
      .foreach(println)

    def filter1(line:String)={
      val str = line.split("\\s+")
      //0.1 数据长度不足
      str.length > 14 &&
        //0.2 重要字段缺失
        str(8).length == 3
    }
    def writeOut(line:String)={
      val str = line.split("\\s+")
      val ip = str(0)
      val time = DateFormatter.dateFormat(str(3).tail)
      val rCode = str(8).toInt
      val osType = str(12).toLowerCase
      (ip,time,rCode,osType)
    }
  }
}
