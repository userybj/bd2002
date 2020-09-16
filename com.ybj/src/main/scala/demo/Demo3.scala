package demo

import scala.collection.mutable

object Demo3 {
  def main(args: Array[String]): Unit = {
    /**
     * 在scala中没有预定义的算数运算符
     * 所以scala中将算数运算符的符号作为函数名
     * 用来定义值类型之间的计算函数
     */
    def <====|==(): Unit ={
      println("<====|==这是一把大宝剑!!!")
    }

    /**
     * 在Scala中import除了可以导入类的依赖
     * 还可以将类中的方法进行导入
     * 调用方法就可以省略类名不写
     */
    // import可以定义在任意位置
    def f3(): Unit ={
      import math._
      println(sqrt(2))
    }
    f3()
    <====|==
    //定义一个长度为5的数组
    val array = new Array[Int](5)
    array(0) = 2

    //定义一个map集合
    val map = new mutable.HashMap[String,String]()
    map.put("k1","v1")
    val maybeString = map.get("k1")
    println(maybeString)  //输出  Some(v1)
    println(maybeString.get) //输出 v1
    /**
     * Option是Scala中为了表示在有可能有多种结果(有值或者无值)的情况
     * 使用统一的对象进行返回的专门的方式
     * Option有两类子类: Some[]是有值 和 None是无值
     */
    val str = map.getOrElse("k2","v2")
    println(str)
  }
}
