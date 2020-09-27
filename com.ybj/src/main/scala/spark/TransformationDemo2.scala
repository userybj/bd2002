package spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

object TransformationDemo2 {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf()
      .setMaster("local[4]")
      //spark UI 中对应的执行项目的名称
      .setAppName("demo2"))


    //11. reduceByKey
    /**
     * 声明def reduceByKey(func: (V, V) => V): RDD[(K, V)]
     * 参数  二元函数  第一次拿到两个V进行聚合，下次每次拿到聚合结果和新的V再聚合
     * 返回值 RDD[(K, V)] 和原RDD类型一致
     * 作用  对pairRDD按照Key聚合Value
     */
    val rdd1:RDD[String] = sc.makeRDD(Array("a","b","c","a","b","d","a","b","c","c","a","b"))
    //rdd1转换成一个(String,1)的二元组
    rdd1.map((_,1))
      //按照key进行聚合
      .reduceByKey(_+_)
     // .foreach(println)
    //结果  (c,3)  (d,1)  (b,4)  (a,4)


    //12. groupByKey
    /**
     * 声明   def groupByKey(): RDD[(K, Iterable[V])] =
     * 参数
     * 返回值  K和原RDD相同，Value是一个原RDD的V泛型的Iterable
     * 作用   将pairRDD中的数据按照Key进行分组
     */
    rdd1.map((_,1))
      .groupByKey()
      //.foreach(println)
    //结果   (c,CompactBuffer(1, 1, 1))
    //       (b,CompactBuffer(1, 1, 1, 1))
    //       (a,CompactBuffer(1, 1, 1, 1))
    //       (d,CompactBuffer(1))



    //13. combineByKey
    /**
     * 声明  def combineByKey[C](
     * createCombiner: V => C,      //创建合并器
     * mergeValue: (C, V) => C,     //合并value
     * mergeCombiners: (C, C) => C  //聚合合并器
     * ): RDD[(K, C)]
     * 参数
     * 返回值    RDD[(K, C)] Key和原RDD相同，V变成了C类型
     * 作用    对RDD按照Key进行聚合，但是聚合过程中V的类型可以改变
     * 所有的聚合，底层都是使用combineByKey或者aggregateByKey
     * 因为分布式数据集RDD的数据，是保存在不同机器的，需要在每个机器先预聚合，
     * 最终再进行全局的聚合
     */
    // rdd1中字母的平均个数
    val rdd2:RDD[(Int,(Int,Int))] = rdd1.map((_,1))
      .reduceByKey(_+_)
      //变身为(1,(String,Int))便于统计所有字母个数
      .map((1,_))
      //                              字母的次数 计数字母个数
      .combineByKey((v:(String,Int)) => (v._2 , 1),
        //                         字母次数的预聚合  字母个数预聚合
        (c:(Int,Int),v:(String,Int)) => (c._1+v._2,c._2+1),
        //                             字母次数聚合    字母个数聚合
        (c1:(Int,Int),c2:(Int,Int)) => (c1._1+c2._1,c1._2+c2._2)
      )
    rdd2.map(x => x._2._1/x._2._2)
      //.foreach(println)       //结果    3
    /**
     * reduceByKey()是combineByKey()的一种特殊情况
     * rdd1.reduceByKey(_+_)等价于下
     * rdd1.combineByKey(
     * (v: Int) => v,
     * (c: Int, v: Int) => c + v,
     * (c1: Int, c2: Int) => c1 + c2)
     */


    //14. aggregateByKey
    /**
     * 声明  def aggregateByKey
     *   (zeroValue: U)
     *   (seqOp: (U, V) => U,
     *    combOp: (U, U) => U
     *   ): RDD[(K, U)] =
     * 参数  第一个参数列表，类似combineByKey中的第一个函数，
     * 但是aggregateByKey的合并器，不需要使用原RDD中的Value，可以直接声明
     * 第二个参数列表，类似combineByKey中的第二第三个函数，作用相同
     * 返回值  RDD[(K, U)] 和原RDD相同K，v变成U泛型
     * 作用   分布式的聚合，类似combineByKey
     */
    val rdd3:RDD[(Int,(Int,Int))] = rdd1.map((_,1))
      .reduceByKey(_+_)
      .map((1,_))
      .aggregateByKey((0,0))((u:(Int,Int),v:(String,Int)) => (u._1+v._2,u._2+1),
        (u1:(Int,Int),u2:(Int,Int)) => (u1._1+u2._1,u1._2+u2._2))
    //rdd3.foreach(println)     //结果  (1,(12,4))



    //15. foldByKey  和aggregateByKey的关系，就像reduceByKey和combineByKey的关系
    // 当 aggregateByKey聚合的后两步函数，完全相同时，可以简化写成foldByKey
    /**
     * 声明  def foldByKey(zeroValue: V)(func: (V, V) => V): RDD[(K, V)] =
     * 参数  两个参数列表，
     * 返回值  RDD[(K, V)]
     * 作用
     */


    // 排序相关
    //16. sortByKey
    /**
     * 声明 def sortByKey(ascending: Boolean = true,
     *   numPartitions: Int = self.partitions.length): RDD[(K, V)] =
     * 参数    第一个参数是一个boolean     true升序，false降序
     *         第二个参数是分区数，排序后转换为多少个分区，默认和原RDD分区数一致
     * 返回值 RDD[(K, V)] 和原RDD类型相同
     * 作用   对pairRDD按照Key进行排序
     */
    rdd1.map((_,1))
      //按照k进行聚合
      .reduceByKey(_+_)
      //k和v位置调换
      .map(x => (x._2,x._1))
      //按照k进行升序排序
      .sortByKey()
      .map(x => (x._2,x._1))
      //.foreach(println)       //结果    (d,1)  (c,3)  (a,4)   (b,4)



    //17. sortBy
    /**
     * 声明  def sortBy(
     * f: (T) => K,    //分区条件
     * ascending: Boolean = true,  //true升序，false降序
     * numPartitions: Int = this.partitions.length): RDD[T]
     * 参数
     * 返回值
     * 作用
     */
    rdd1.map((_,1))
      .reduceByKey(_+_)
      .sortBy(x => x._2)
      //.foreach(println)     //结果  (d,1)  (c,3)  (a,4)  (b,4)



    //18.  join   内连接
    /**
     * 声明 def join[W](other: RDD[(K, W)]): RDD[(K, (V, W))] =
     * 参数   另外一个RDD K和原RDD类型相同  v和原RDD类型可以不同
     * 返回值   RDD[(K, (V, W))]二元组，二元组的K是原RDD的K类型，
     *          二元组的V是一个二元组，Key是原RDD的V类型，value是参数RDD的V类型
     * 作用  将两个KV的RDD按照Key进行内连接
     */
    val rdd4:RDD[(Int,String)] = sc.makeRDD(Array(1 -> "🐱",2 -> "🐕",3 -> "🐀",4 -> "🐂"))
    val rdd5:RDD[(Int,String)] = sc.makeRDD(Array(1 -> "小花",2 -> "阿黄",2 -> "旺财",2 -> "小黑",4 -> "大黄"))
    rdd4.join(rdd5)
      //.foreach(println)   //结果  (1,(🐱,小花))   (4,(🐂,大黄))   (2,(🐕,阿黄))
                          //        (2,(🐕,旺财))   (2,(🐕,小黑))



    //19.  leftOuterJoin
    //1.声明 def leftOuterJoin[W](other: RDD[(K, W)]): RDD[(K, (V, Option[W]))] =
    rdd4.leftOuterJoin(rdd5)
      //.foreach(println)            //结果  (3,(🐀,None))  (4,(🐂,Some(大黄)))  (1,(🐱,Some(小花)))
    //                                     (2,(🐕,Some(阿黄)))  (2,(🐕,Some(旺财)))  (2,(🐕,Some(小黑)))



    //20. rightOuterJoin
    //1.声明 def rightOuterJoin[W](other: RDD[(K, W)]): RDD[(K, (Option[V], W))] =
    rdd4.rightOuterJoin(rdd5)
     // .foreach(println)        //结果 (4,(Some(🐂),大黄))  (2,(Some(🐕),阿黄))  (1,(Some(🐱),小花))
    //                                  (2,(Some(🐕),旺财))  (2,(Some(🐕),小黑))



    //21. fullOuterJoin
    //1. 声明 def fullOuterJoin[W](other: RDD[(K, W)]): RDD[(K, (Option[V], Option[W]))] =
    rdd4.fullOuterJoin(rdd5)
     // .foreach(println)         //结果 (3,(Some(🐀),None))  (1,(Some(🐱),Some(小花)))  (2,(Some(🐕),Some(阿黄)))
    //                                   (4,(Some(🐂),Some(大黄)))  (2,(Some(🐕),Some(旺财)))  (2,(Some(🐕),Some(小黑)))



    //22. cogroup
    /**
     * 声明 def cogroup[W](other: RDD[(K, W)]): RDD[(K, (Iterable[V], Iterable[W]))] =
     * 参数  另外一个pairRDD
     * 返回值  返回值 RDD[(K, (Iterable[V], Iterable[W]))]
     * 作用  对RDD先groupByKey再fullOuterJoin ,区别于fullOuterJoin的是，
     *       不会以Option类型作为结果，而是直接将Some的值取出，将None变成空的集合
     */
    val rdd6: RDD[(Int, String)] = sc.makeRDD(List(1 -> "1a", 1 -> "1b", 2 -> "2a"))
    val rdd7: RDD[(Int, Double)] = sc.makeRDD(List(1 -> 1.1, 3 -> 3.1, 3 -> 3.2))
    rdd6.groupByKey().fullOuterJoin(rdd7.groupByKey())
    // .foreach(println)        //结果 (2,(Some(CompactBuffer(2a)),None))  (3,(None,Some(CompactBuffer(3.1, 3.2))))
    //                                 (1,(Some(CompactBuffer(1a, 1b)),Some(CompactBuffer(1.1))))
    rdd6.cogroup(rdd7)
    //  .foreach(println)           //结果 (2,(CompactBuffer(2a),CompactBuffer()))  (3,(CompactBuffer(),CompactBuffer(3.1, 3.2)))
    //                                     (1,(CompactBuffer(1a, 1b),CompactBuffer(1.1)))



    //23. cartesian 笛卡尔积
    /**
     * 声明 def cartesian(other: RDD[U]): RDD[(T, U)]
     * 参数  另外一个RDD
     * 返回值 二元组作为泛型的RDD   k是原RDD的元素，v是参数RDD的元素
     * 作用  对两个RDD的元素进行两两组合
     */



    //25. coalesce  复写合并(重新分区)
    /**
     * 声明  def coalesce(
     * numPartitions: Int,            //设置新的分区数
     * shuffle: Boolean = false,      // 是否进行shuffle
     * partitionCoalescer: Option[PartitionCoalescer] = Option.empty): RDD[T] =
     * 参数 分区数
     * 返回值  和原RDD类型相同
     * 作用  重新调整分区数量  默认不开启shuffle
     */
    sc.makeRDD(1 to 10, 10)
      .coalesce(1, true)
     // .foreach(println)



    //26. repartition
    /**
     * 声明def repartition(numPartitions: Int): RDD[T] =
     *     实际调用coalesce(numPartitions, shuffle = true)默认不shuffle
     * 参数   分区数
     * 返回值  和原RDD类型相同
     * 作用 重新调整分区数量 ，默认开启shuffle
     */
    sc.makeRDD(1 to 10, 10)
      .repartition(1)




    //26. repartitionAndSortWithinPartitions
    //  def repartitionAndSortWithinPartitions(partitioner: Partitioner): RDD[(K, V)] =
    //调整分区时如果要自定义分区器 并且需要排序，可以直接使用这个算子来代替
    //rdd.partitionBy(Partitioner)
    //  .sortByKey()
    /**
     * Partitioner默认有两个实现
     * HashPartitioner
     * 如果很多key都相同 -> hash值就相同 -> 数据倾斜
     * RangePartitioner
     * RangePartitioner使用抽样算法轮询进行分区，
     * 可以保证每个分区的元素数量差不多
     */
    sc.makeRDD(1 to 10, 2)
      .map(x => (x, x))
      .repartitionAndSortWithinPartitions(new HashPartitioner(2))




    //27. glom
    /**
     * 声明 def glom(): RDD[Array[T]] =
     * 参数
     * 返回值 RDD[Array[T]]
     * 作用  将每个分区的数据，一次性取出放到一个Array中
     * 再将这些Array放到一个新的RDD中
     */
    sc.makeRDD(1 to 10, 2)
      .glom()
     // .foreach(x => println(x.toList))     //结果  List(6, 7, 8, 9, 10)  List(1, 2, 3, 4, 5)



    //28. mapValues
    /**
     * 声明 def mapValues[U](f: V => U): RDD[(K, U)] =
     * 参数  一元函，将V类型变成了U类型
     * 返回值 和原RDD有相同的K，V变成了f的返回值类型
     * 作用  对于KV类型的RDD，不管K只操作value
     */
    sc.makeRDD(1 to 10)
      //变身为RDD的二元组
      .map(x => (x,x))
      //二元组每一个v值加1
      .mapValues(x => x + 1)
      //.foreach(println)

      //  效率略高于下面的操作
      //.map(x => (x._1, x._2 + 1)).foreach(println)



    //29. subtract A-(A∩B)
    /**
     * 声明def subtract(other: RDD[T]): RDD[T] =
     * 参数   另外一个和原RDD类型相同的RDD
     * 返回值  RDD[T]和原RDD类型相同
     * 作用
     */
    sc.makeRDD(1 to 6)
      .subtract(sc.makeRDD(5 to 10))
      //.foreach(println)      //结果   1  2  3  4



    //24. pipe 可以将linux命令(shell脚本) 引用到spark中参与运算
    /**
     * 声明def pipe(command: String): RDD[String] =
     * 参数  一个命令或者脚本的路径(hdfs://)
     * 返回值  RDD[String]
     * 作用  将原RDD的每个分区单独调用一次脚本
     * 调用时将分区中的元素逐个传入脚本中的read命令
     * 并将脚本的所有echo收集起来以String类型放到新的RDD中
     */
  }
}
