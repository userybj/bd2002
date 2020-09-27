package structure

import java.util
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object ArrayDemo {
  def main(args: Array[String]): Unit = {
    //一、数组
    //1.定长数组  Array
    //1.1 创建实例
    val arr1: Array[Int] = new Array[Int](10)
    val arr2: Array[Int] = Array(1, 2)
    //1.2 取值
    val i = arr1(3)
    //1.3 赋值
    arr1(3) = 666
    //1.4 遍历
    for (i <- arr1) i //直接获取元素
    for (i <- arr1.indices) arr1(i) //根据下表获取元素
    //1.5 toString问题
    // 因为Array没有友好的toString
    // 需要观察时可以直接转成其他有toString的集合
    //    println(arr1.toList)
    // 想将集合中的所有元素，以特定的符号进行分隔
    // 拼接成一个字符串
    val str = arr1.mkString("|")

    //2.变长数组 ArrayBuffer
    //2.1 创建实例
    val buf1 = new ArrayBuffer[Int]()
    val buf2 = ArrayBuffer[Int]()
    //2.3 赋值
    buf1.append(111) //追加元素
    buf1 += 222 //赋值
    buf1(1) = 333
    buf1.remove(0)
    //2.2 取值
    //    println(buf1(0))
    //    println(buf1(1))
    //2.4 遍历
    // 直接遍历元素
    for (i <- buf1) i
    // 索引遍历
    for (i <- buf1.indices) buf1(i)
    //2.5 toString问题
    //println(buf1)

    //3.多维数组
    val array = Array.ofDim[Int](2, 2)

    //4.java和scala的数组转换
    //导入转换器依赖
    import scala.collection.JavaConverters._

    // Java的ArrayList转换成Scala的ArrayBuffer
    val juList1: java.util.List[String] = new util.ArrayList[String]()
    val buf3: mutable.Buffer[String] = juList1.asScala

    // Scala的ArrayBuffer转换成Java的ArrayList
    val buf4: mutable.Buffer[String] = new ArrayBuffer[String]()
    val juList2: java.util.List[String] = buf4.asJava

    //二、元组 tuple
    // 有些时候，我们的函数需要返回多个值
    // 比如 写一个函数，返回a/b的商 和 余数

    def f1(a: Int, b: Int) = {
      val q = a / b
      val r = a % b
      (q, r)
    }
    val tuple:(Int,Int) = f1(10,-4)
    //println(tuple)
    //1.1 创建实例
    // 元组是一种特殊的集合
    // 形态 就是一个小括号括起来的若干(2,22)的元素
    // 元组可以为每一个元素记录他的类型
    val tuple1:(Int,Double,String,Char) = (1,2.5,"土豆",'人')
    //1.2 取值
    // 元组对象调  _序号  序号从1开始，表示元组中的按照顺序存储的元素
    val value = tuple1._1
    //println(value)

    //三、映射 Map
    // map描述一一对应的关系
    // 关系中有key和value  刚好和二元组的形态一样
    // 二元组由叫做对偶，就将对偶作为Map中保存的元素
    //1.1 创建实例
    val map = new mutable.HashMap[String,String]()
    val map1 = Map(("k11","v11"),("k22","v22"))
    Map("k1" -> "v1","k2" -> "v2","k3" -> "v3")
    //1.2 集合赋值
    map.put("k1","v1")
    map.+=(("k2","v2"),("k3","v3"))
    map += "k4" -> "v4"
    //1.3 集合取值
    //get()方法通过k得到的v不知道数据类型
    val maybeString = map.get("k1")
    val str1 = map.getOrElse("k1","ss")
    val str2 = map("k4")
    //println(maybeString+"*******"+str1+"^^^^^^"+str2)
    //1.4 遍历
    for (x <- map) {
      // 遍历map中保存的二元组
      val k = x._1
      //println(k)
      val v = x._2
      //println(v)
    }
    for ((k, v) <- map) {
      //println(k)
      //println(v)
    }
    //1.5 tostring
    //println(map)
    //输出结果: Map(k2 -> v2, k4 -> v4, k1 -> v1, k3 -> v3)

    //List集合
    //1.1 创建实例
    val list = List(1,2,3)
    val list1 = List()
    //1.2 拼接
    /**
     * 因为List是不可变集合，所以不能添加元素到原有的List中
     * 那么如果想给List"添加"元素，
     * 可以将原有List和新元素组成一个新的List
     */
    val list2 = list1 :+ 1  //集合后追加
    //println(list+"***"+list2)
    val list3 = 2 +: list2  //集合前追加
    //println(list3)    //输出:List(2, 1)
    val list4 = list3 ++ list2  //集合的拼接
    //println(list4)      //输出结果:List(2, 1, 1)
    //通过拼接多个元素创建list集合
    /**
     * 使用 :: 方法 左边是元素，右边是List
     * 可以将元素拼接到List中
     * 这种表达式，最后一个位置  必须是一个List
     * 特殊语法   Nil代表一个空的List
     */
    val list5 = 1 :: 2 :: 3 :: 6 :: list
    val list6 = 111 :: 222 :: 333 :: 666 :: 88 ::Nil
    //println(list5)    //输出结果:List(1, 2, 3, 6, 1, 2, 3)
    //1.3 取值
    //println(list5(3))     //索引为3的元素
    //println(list5.head)   //头元素
    //println(list5.last)   //最后一个元素
    //println(list5.init)   //除最后一个元素以外的元素
    //println(list5.tail)   //除头元素外的元素
    //1.4 遍历
    /*for (num <- list5){
      println(num)
    }*/
    //list5.foreach((x: Int) => print(x))
    //简化
    //list5.foreach(print)
    //1.5 toString问题

    //Set集合
    //1.1 创建实例
    val set = Set(1,2,3,1,5)
    val set1 = new mutable.HashSet[Int]()
    //1.2 赋值
    set1.add(1)
    set1 add 1
    set1 += 2
    //println(set1)
    //1.3 取值
    //由于set集合是无需的,一般不用索引去取值
    val bool = set1(2) //判断集合中是否存在元素
    //println(bool)
    //1.4 遍历
    for (num <- set){
      //println(num)
    }
    //1.5 toString问题

    // 队列
    //是一种先进先出的数据结构
    //1.1 创建实例
    val queue1 = new mutable.Queue[Int]()
    val queue2 = mutable.Queue[Int]()
    //1.2 赋值
    queue1 += 1
    queue1 += 2
    queue1 += 3
    queue1 += 4
    queue1 += 6
    //1.3 取值
    val seq = queue1.dequeueAll((x:Int) => x%2 ==0)
    // 上面的简写: queue1.dequeueAll(_ % 2 == 0)
    //println(seq)
    //1.4 遍历
    //队列可以当成是一个集合,所以与集合的遍历类似
    for (num <- queue1){
      //println(num)
    }
    //1.5 toString问题

    //堆栈  stack
    //1.1 创建实例
    val stack = new mutable.Stack[Int]()
    val stack1 = mutable.Stack(1,2,3,4)
    //1.2 入栈
    stack.push(1)
    stack.push(3)
    stack.push(2)
    //1.3 出栈
    val res = stack1.pop()
    val res1 = stack.pop()
    //println(res+"****"+res1)   //输出:  1****2
  }
}
