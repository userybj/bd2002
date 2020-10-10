package sparkSql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkSqlDemo2 {
  def main(args: Array[String]): Unit = {
    //1.获取SparkSQL的程序入口
    val spark = SparkSession.builder()
      .appName("sparksql")
      .master("local[2]")
      .getOrCreate()
    import spark.implicits._
    val sc = spark.sparkContext
    val rdd1:RDD[String] = sc.textFile("C:\\Users\\ASUS\\Desktop\\douyin.txt")
    val df1:DataFrame = rdd1.map(x => {
      val strs = x.split("\\t")
      (strs(0), strs(1), strs(2), strs(3), strs(4), strs(5), strs(6), strs(7), strs(8),
        strs(9), strs(10), strs(11), strs(12), strs(13), strs(14), strs(15), strs(16), strs(17))
    }).toDF("aweme_id", "city", "feed_desc", "create_time", "video_url", "music_url",
      "music_author", "digg_count", "comment_count", "share_count", "user_id", "user_short_id",
      "nickname", "gender", "birthday", "avatar", "signature", "share_url")
   // df1.show()
    //df1.write.format("csv").save("C:\\Users\\ASUS\\Desktop\\test")
    df1.write.save("C:\\Users\\ASUS\\Desktop\\out4")
  }
}
