package spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}
import utils.DateFormatter

object TransformationDemo1 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("woshishui")
    val sc = new SparkContext(conf)
    // 转换算子transformation
    // RDD -> transformation -> RDD

    //1.map
    /**
     * 声明    def map(f: T => U): RDD[U]
     * 参数    一个一元函数，参数是原RDD的元素类型，返回值可以改变类型
     * 返回值  新的RDD，泛型是f的返回值类型
     * 作用    将原RDD中的每个元素应用到f中，将返回值收集到一个新的RDD中
     */
    val rdd1:RDD[String] = sc.textFile("C:\\Users\\ASUS\\Desktop\\log\\logs\\access.log-20190924")
    val rdd2:RDD[(String,String,Int,String)] = rdd1.map(line => {
      var strs = line.split("\\s+")
      val ip = strs(0)
      val time = DateFormatter.dateFormat(strs(3).tail)
      var status = 0
      try {
        status = strs(8).toInt
      } catch {
        case e: Exception =>
      }
      val user = strs(12)
      (ip, time, status, user)
    })
    //rdd2.foreach(println)

    //2.filter
    /**
     * 声明    def filter(f: T => Boolean): RDD[T]
     * 参数    一元函数，参数是原rdd的中的元素，返回值是Boolean
     * 返回值  和原RDD泛型一致的RDD
     * 作用    过滤元素 (f返回True的元素将被留下)
     */
    //rdd2.filter(x => x._3 == 200)
    val rdd3:RDD[(String,String,Int,String)] = rdd2.filter(_._3 == 200)
    //rdd3.foreach(println)


    //3.flatMap
    /**
     * 声明def flatMap(f: T => TraversableOnce[U]): RDD[U]
     * 参数 一个一元函数，参数是原RDD的元素类型，返回值是一个集合
     * 返回值 是一个f的返回值泛型的RDD
     * 作用 将二维集合变成一维集合
     */
    //统计所有ip中0-9的数字的出现次数
    rdd3.flatMap(x => {
      val res1:String = x._1
      val res2:String = res1.replaceAll(".","")
      val res3:Array[String] = res2.split("")
      res3
    })
    val rdd4:RDD[String] = rdd3.flatMap(_._1.replaceAll(".","").split(""))


    //4.mapPartitions
    /**
     * 1.声明  def mapPartitions(f: Iterator[T] => Iterator[U],
     * preservesPartitioning: Boolean = false): RDD[U]
     * 2.参数
     * 第一个参数
     * 一个一元函数，参数是一个原RDD泛型的迭代器，
     * 这个迭代器每次传入一个分区的全部元素
     * 返回值也是一个迭代器，泛型不限
     * 3.返回值  RDD[U]  泛型为f返回值泛型的RDD
     * 4.作用
     * 之前map是每次拿到一个元素
     * mapPartitions一次性拿到一个分区的元素
     * 在将处理完的结果放回到新的RDD中
     */
    val rdd5:RDD[Int] = sc.makeRDD(1 to 10)
    val rdd6:RDD[String] = rdd5.mapPartitions(iter => Iterator(iter.toList.mkString("^")))
    rdd5.map(_ + 1) //   RDD运算了10次
    rdd5.mapPartitions(iter => iter.map(_ + 1)) //  RDD运算了2
    //rdd6.foreach(println)
    //结果： 1^2^3^4^5
    //       6^7^8^9^10


    //5.mapPartitionsWithIndex
    /**                                  分区索引
     * 声明  def mapPartitionsWithIndex(f: (Int, Iterator[T]) => Iterator[U],
     * preservesPartitioning: Boolean = false): RDD[U]
     * 参数   与mapPartitions类似，多了一个int类型的参数，接收传进来的分区索引
     * 返回值 RDD[U]
     * 作用  与mapPartitions类似
     */
    val rdd7:RDD[(Int,String)] = rdd5.mapPartitionsWithIndex((index, iter) => {
      val temp: (Int, String) = (index, iter.mkString("*"))
      Iterator(temp)
    })
    //rdd7.foreach(println)
    //结果   (0,1*2*3*4*5)
    //       (1,6*7*8*9*10)


    //6.sample
    // 计算机的随机算法，可以保证如果输入参数不同-->>输出结果不同
    /**
     * 声明  def sample(
     * withReplacement: Boolean,            //是否放回  false为不放回
     * fraction: Double,                    //抽样比例 不是非常精准
     * seed: Long = Utils.random.nextLong   //随机种子
     * ): RDD[T]
     * 参数
     * 返回值 和原RDD类型相同的RDD
     * 作用  在海量数据中进行抽样
     */
    val rdd8:RDD[Int] = sc.makeRDD(0 to 100)
    val rdd9:RDD[Int] = rdd8.sample(false,0.5)   //sample()返回一个RDD
    val ints = rdd9.collect()
    //println(ints.toList)
    val res2: Array[Int] = sc.makeRDD(1 to 100)
      .takeSample(false, 10)   //takeSample()方法返回一个集合


    //7.union   RDDA ∪ RDDB
    /**
     * 声明   def union(other: RDD[T]): RDD[T]
     * 参数    另外一个和原RDD类型相同的RDD
     * 返回值  和原RDD类型相同
     * 作用   合并RDD
     */
    val res3:Array[Int] = sc.makeRDD(1 to 5)
      .union(sc.makeRDD(3 to 6))
      .collect()
    //println(res3.toList)      //结果  List(1, 2, 3, 4, 5, 3, 4, 5, 6)


    //8.intersection   RDDA ∩ RDDB
    /**
     * 声明    def intersection(other: RDD[T]): RDD[T]
     * 参数    另外一个和原RDD类型相同的RDD
     * 返回值  和原RDD类型相同
     * 作用    交集
     */
    val rdd10:RDD[Int] = sc.makeRDD(1 to 6).intersection(sc.makeRDD(3 to 10))
    //println(rdd10.collect().toList)     //结果  List(4, 6, 3, 5)


    //9  distinct
    /**
     * 声明  def distinct(): RDD[T] =
     *       def distinct(numPartitions: Int): RDD[T] =
     * 参数
     * 返回值
     * 作用    去重
     */
    val rdd11:RDD[Int] = sc.makeRDD(Array(1,3,1,4,2,5,2,6,3)).distinct()
    //rdd11.foreach(println)      //结果 1  4  3  6  5  2


    //10 partitionBy   用于RDD泛型为二元组的
    /**
     * 声明 def partitionBy(partitioner: Partitioner): RDD[(K, V)]
     * 参数  是一个分区器对象的实例
     * 返回值  和原RDD类型相同
     * 作用   按照自定的分区器，对RDD中的元素进行分区
     */
      //把1~10按照奇偶分区
    val rdd12:RDD[(Int,Int)] = sc.makeRDD(1 to 10).map(x => (x , x))
    rdd12.partitionBy(new Partitioner {
      override def numPartitions: Int = 2
      override def getPartition(key: Any): Int = {
        key.asInstanceOf[Int]%2
      }
      //取出二元组中的k
    }).map(_._1)
      //根据索引去分区
      .mapPartitionsWithIndex((index,iter) => Iterator((index,iter.mkString("#"))))
    //  .foreach(println)
    //结果     (1,1#3#5#7#9)
    //        (0,2#4#6#8#10)
  }
}
