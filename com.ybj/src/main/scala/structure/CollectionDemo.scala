package structure

import java.io.File

import javax.sound.sampled.Line
import org.apache.commons.io.FileUtils

import scala.collection.mutable

object CollectionDemo {
  def main(args: Array[String]): Unit = {
    //1.map
    /**声明：def map(f: A => B): That =
    * 参数： 一元函数 ， 参数是原集合的泛型，返回值是任意类型
    * 返回值：和原集合类型相同的集合
    * 作用： 将集合中的每一个元素应用到f中，并将返回值收集到一个新的集合中
     */
    val list = List[Int](1,3,6)
    val array = new Array[Int](3)
    //需求  list中的每一个元素变为原来的一倍
    //一般方法
    /*for (i <- list.indices){
      array(i) = 2 * list(i)
    }
    println(array.toList)*/
    //通过map算子
    val list1 = list.map((x:Int) => x * 2)
    //上式简化: list.map(_ * 2)
    //println(list1)
    //需求 将日志中的所有IP获取出来

    //① 读取文件
    //val lines = FileUtils.readFileToString(new File("C:\\Users\\Administrator\\Desktop\\logs\\access.log"))
    //② 切出每一行
    //val linesArr: Array[String] = lines.split("\n")
    //③ 获取IP
    def f1(line: String)={
      line.split("\\s+")(0)
    }
    //linesArr.map(f1)
    //linesArr.map((line:String) =>line.split("\\s+")(0))
    //简写为: linesArr.map(_ .split("\\s+")(0))

    //2.flatMap
    /**声明：def flatMap(f: A => GenTraversableOnce[B]): That =
    * 参数： 一元函数   参数是原来集合的泛型  返回值必须是一个集合，并且这个集合的泛型可以和原集合不同
    * 返回值：和原集合类型一样，泛型是B类型
    * 作用：将二维集合变成一维集合
     */
    val array1 = Array(Array("a", "c", "e", "g"), Array("b", "d", "f", "h"))
    val array2 = array1.flatMap((x:Array[String]) => x:Array[String])
    //println(array2.toList)

    //①读文件
    //val hpLines = FileUtils.readFileToString(new File("C:\\Users\\Administrator\\Desktop\\HarryPotter.txt"))
    //②切出每个单词
    //val lines1: Array[String] = hpLines.split("\n")
    //                          String => Array[String]
    //val res6: Array[Array[String]] = lines1.map(_.split("\\s+"))
    //                          String => Array[String]
    /*val res7: Array[String] = lines1
      .flatMap(_.split("\\s+"))
      .map(_.toLowerCase.replaceAll("\\W", ""))
     */
    //③将单词进行计数   Map[String,Int]()
    val map = new mutable.HashMap[String, Int]()
    /*for (word <- res7) {
      map.put(word, map.getOrElse(word, 0) + 1)
    }*/

    //def foreach[U](f: A => U): Unit =
    // 将集合中的每一个元素 依次传入f中
    // foreach仅仅是遍历，不收集返回值
    /*res7.foreach { x =>
      map.put(x, map.getOrElse(x, 0) + 1)
    }*/

    //reduceLeft()和reduceRight()算子
    // def reduceLeft(op: (B, A) => B): B
    // def reduceRight(op: (A, B) => B): B
    val array3 = Array(1,2,3,6)
    //需求：数组求和

    //一般方法
    val sum = array3.sum
    //println(sum)

    //通过reduceLeft()算子
    val res3 = array3.reduceLeft(_ + _)
    //println(res3)
    val res4 = array3.reduceRight(_ - _)
    //println(res4)    //结果：-4

    //foldLeft()()和foldRight()()算子
    //            第一个参数列表传入聚合时的初始值
    // def foldLeft[B](z: B)(op: (B, A) => B): B =
    // def foldRight[B](z: B)(op: (A, B) => B): B =
    val res5 = array3.foldLeft(100)(_ - _)
    //println(res5)    //结果：88
    val res6 = array3.foldRight(100)(_ - _)
    //println(res6)      //结果：96

    //zip()算子
    //zip 将A集合元素作为二元组的K，B集合元素作为二元组的V
    //获取一个原来类型，二元组泛型的新集合
    val list2 = List(1,2,3,4)
    val list3 = List("大","中","小")
    val tuples = list2 zip list3
    //println(tuples)    //结果：List((1,大), (2,中), (3,小))

    //迭代器，可以用于更安全的数据访问
    //游标会单调递增，所以元素只能访问一次
    val iterator = list2.toIterator
    /*while (iterator.hasNext){
      println(iterator.next())
    }*/

    // filter()算子
    // 将集合中的每个元素传入p函数
    // 保留返回值为true的元素
    //def filter(p: A => Boolean): TraversableOnce[A] =
    val list4 = list2.filter(_%2 != 0)
    //println(list4)    //结果：List(1, 3)

    //take()算子
    val list5 = list2.take(2)
    //println(list5)      //结果：List(1, 2)
    val str = "abcdefg".takeRight(3)
    val str1 = "abcdefg".take(3)
    println(str)          //结果:efg
    println(str1)         //结果:abc
  }
}
