package spark

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

object SparkCoreDemo {
  def main(args: Array[String]): Unit = {
    var conf: SparkConf = new SparkConf()
      .setAppName("woshishui")
      .setMaster("local[4]")
    val sc = new SparkContext(conf)

    //RDD主要有四类操作
    //1. 用sc创建RDD
    //1.1 通过读取文件创建
    /**
     * sc.textFile("")     将文件中的每一行当作一个String 保存在RDD中
     * sc.sequenceFile("")
     * sc.objectFile("")
     */
    //1.2 通过集合创建RDD      makeRDD()
    val rdd1:RDD[Int] = sc.makeRDD(Array(1,2,3,4,5,6))
    /**
     * def makeRDD(
     *    seq: Seq[T],  //传入的集合
     *    numSlices: Int = defaultParallelism  分区的数量
     *                     默认值是当前可用核心数
     *    ): RDD[T]
     */
    //重新分区    repartition()
    val rdd2:RDD[Int] = rdd1.repartition(10)
    //获取分区数    getNumPartitions
    val p1 = rdd1.getNumPartitions
    val p2 = rdd2.getNumPartitions
    //println("p1="+p1+"*********p2="+p2)    //结果:p1=4*********p2=10


    //2. 缓存算子

    //2.1 第一种方式直接调用cache，
    // cache()其实就是调用空参的persist()
    //rdd1.cache()

    //2.2 空参的persist()
    // 其实调用了persist(StorageLevel.MEMORY_ONLY)
    //  也就是说默认使用仅内存的方式进行缓存
    //rdd1.persist()

    //2.3 有参的persist()可以传入一个持久化的级别
    // StorageLevel
    //rdd1.persist(StorageLevel.MEMORY_AND_DISK)

    //2.4 checkpoint持久化
    // 缓存算子在spark程序结束之后会清除缓存
    // 如果想让某个RDD的数据永久保存，
    // 可以使用checkpoint将他保存到例如HDFS的这种持久化平台上
    //sc.setCheckpointDir("hdfs://bd01:9000/xxx")
    //rdd1.checkpoint()

    //3. 转换算子transformation
    // RDD -> transformation -> RDD


    //4. 行动算子action
    // RDD -> action -> (文件、scala集合、变量、外部存储...)


    // RDD的懒执行特点
    // 只有遇到action算子，
    // RDD的血统才会真正开始执行
    //rdd1.map(_+1).map(_+1).map(_+1).saveAsTextFile("")
  }

}
