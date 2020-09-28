package spark

import org.apache.spark.{SparkConf, SparkContext}

object ActionDemo {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf()
      .setMaster("local[2]")
      .setAppName("demo"))

    //1.reduce 将RDD中的元素进行聚合
    // 传入的二元函数 参数和返回值都是原RDD的泛型
    //def reduce(f: (T, T) => T): T =
    val res:Int = sc.makeRDD(1 to 10)
      .reduce(_ + _)
    //println(res)                 //结果:55
    val res1:(Int,Int) = sc.makeRDD(1 to 10)
      .map((_, 1))
      .reduce((x, y) => (x._1 + y._1, x._2 + y._2))
   // println(res1)                   //结果:(55,10)


    //2.collect 将RDD中的元素收集到数组中返回
    // 一般用于测试，如果数据集数量较大，要谨慎操作
    val ints: Array[Int] = sc.makeRDD(1 to 10).collect()


    //3.count 将RDD中元素的数量返回
    val l = sc.makeRDD(1 to 10).count()

    //4.first 将RDD中的第一个元素取出
    sc.makeRDD(1 to 10).first()

    //5.take(n) 按照顺序取前N个元素


    //6.takeSample(是否放回,样本数量) 和sample 类似
    // 对RDD中的元素进行抽样，抽样结果放在Array中返回


    //7.takeOrdered(n) 排序取前几个

    //8.top(n) 排序取前几个

    //9.aggregate 理解上和aggregateByKey类似，但他是action
    //def aggregate
    // (zeroValue: U)
    // (seqOp: (U, T) => U, combOp: (U, U) => U): U
    //    rdd.aggregate()

    //10.fold
    // def fold(zeroValue: T)(op: (T, T) => T): T =
    //    rdd.fold()


    //11. saveAsTextFile()  将RDD的数据保存到文本文件
    sc.makeRDD(1 to 10).repartition(1)
    //      .saveAsTextFile("C:\\Users\\Administrator\\Desktop\\output")

    //12. saveAsSequenceFile()  将RDD的数据保存到序列文件

    //13. saveAsObjectFile()  将RDD保存在对象文件

    //    rdd.saveAsObjectFile("C:\\Users\\Administrator\\Desktop\\output")


    //14.countByKey
    val res3 =  sc.makeRDD(Array(
      1 -> 354,
      1 -> 4356,
      1 -> 62,
      2 -> 66
    )).countByKey()


    //15.foreach

    sc.makeRDD(1 to 10).foreach(println)
  }
}
