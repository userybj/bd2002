package test

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object TestDemo1 extends App {
  //在Scala REPL中，计算3的平方根,然后再对该值求平方。现在，这个结果与3相差多少？
  val num = 3
  var d: Double = math.sqrt(num)
  //println(d)
  var d3: Double = math.pow(d, 2)
  var d1: Double = d * d
  var d2: Double = num - d3
  println(d2) //结果:4.440892098500626E-16
}

object TestDemo2 extends App {
  //在Scala中如何获取字符串“Hello”的首字符和尾字符？
  var str = "hello"
  var head: Char = str.head
  var tail: Char = str.last
  println(head + "*******" + tail)
}

object TestDemo3 extends App {
  //一个数字如果为正数，则它的signum为1;如果是负数,则signum为-1
  // 如果为0,则signum为0.编写一个函数来计算这个值
  def f1(num: Int) = {
    var signum = num match {
      case _ if num > 0 => 1
      case _ if num < 0 => -1
      case _ => 0
    }
    println(signum)
  }

  f1(0)
}

object TestDemo4 extends App {
  //针对下列Java循环编写一个Scala版本:
  //for(int i=10;i>=0;i--)System.out.println(i);
  for (num <- 0 to 10 reverse) {
    println(num)
  }
}

object TestDemo5 extends App {
  //编写一个过程countdown(n:Int)，打印从n到0的数字
  /*def countdown(n:Int)={
    for (num <- 0 to n reverse){
      println(num)
    }
  }
  countdown(5)*/
  def countdowns(n: Int): Unit = {
    (0 to n).reverse.foreach(println)
  }

  countdowns(5)
}

object TestDemo6 extends App {
  //编写一个for循环,计算字符串中所有字母的
  // Unicode代码（toLong方法）的乘积。举例来说，"Hello"中所有字符串的乘积为9415087488L
  /*def f2(str : String): Unit ={
    var number = 1L
    for (char <- 0 until  str.length){
      number *= str(char).toLong
    }
    println(number)
  }
  f2("Hello")   //结果:  9415087488*/
  //使用foreach()
  def f2(str: String) = {
    var number = 1L
    str.foreach(number *= _.toLong)
    number
  }

  //println(f2("Hello"))    //结果:9415087488
  //使用递归方法
  def f3(str: String): Long = {
    if (str.length == 1) str.charAt(0).toLong
    else str.take(1).charAt(0).toLong * f3(str.drop(1))
  }

  //println(f3("Hello"))     //结果:9415087488
}

object TestDemo7 extends App {
  //编写函数计算 ,其中n是整数，使用如下的递归定义:
  /**
   * x^n= x*x^(n-1) ,如果n是正数的话
   * x^0= 1
   * x^-n= 1/(x^n) ,如果n是负数的话
   * 不得使用return语句
   **/
  def power(x: Double, n: Int): Double = {
    if (n == 0) 1
    else if (n > 0) x * power(x, n - 1)
    else 1 / power(x, -n)
  }

  //println(power(3,-3))        //结果:0.037037037037037035
}

object TestDemo8 extends App {
  //编写一段代码，将a设置为一个n个随机整数的数组，要求随机数介于0和n之间。
  def makArr(n: Int): Array[Int] = {
    var array = new Array[Int](n)
    var random = new scala.util.Random()
    //for 循环中的 yield 会把当前的元素记下来，保存在集合中，循环结束后将返回该集合。Scala 中 for 循环是有返回值的
    // 如果被循环的是 Map，返回的就是  Map，被循环的是 List，返回的就是 List
    /**
     * var e = Array(1,2,3,4,5)
     * scala> for (e <- a) yield e
     * res5: Array[Int] = Array(1, 2, 3, 4, 5)
     *
     * scala> for (e <- a) yield e * 2
     * res6: Array[Int] = Array(2, 4, 6, 8, 10)
     *
     * scala> for (e <- a) yield e % 2
     * res7: Array[Int] = Array(1, 0, 1, 0, 1)
     */
    for (i <- array) yield random.nextInt(n)
  }
  //println(makArr(5).toList) //结果:List(3, 0, 0, 4, 4)
}

object TestDemo9 extends App {
  //编写一个循环，将整数数组中相邻的元素置换。
  //比如Array(1, 2, 3, 4, 5)置换后为Array(2, 1, 4, 3, 5)
  def reve(arr:Array[Int]): Array[Int] ={
    //def until(end: Int, step: Int): Range = Range(self, end, step)
    //step为每次的间隔数,默认为1,可以根据自己需要去设置
    for (i <- 0 until (arr.length-1,2)){
      var tepm = arr(i)
      arr(i) = arr(i+1)
      arr(i+1) = tepm
    }
    arr
  }
  //println(reve(Array(1,2,3,4,5,6)).toList)        //结果;List(2, 1, 4, 3, 6, 5)
}

object TestDemo10 extends App {
  //给定一个整数数组，产出一个新的数组，包含原数组中的所有正值，以原有顺序排列
  // 之后的元素是所有零或负值，以原有顺序排列。
  def fArr(arr:Array[Int]): ArrayBuffer[Int] ={
    val buffer = new ArrayBuffer[Int]()
    buffer ++= (for(num <- arr if num > 0) yield num)
    buffer ++= (for(num <- arr if num == 0) yield num)
    buffer ++= (for(num <- arr if num < 0)yield num)
    //追加,但是无返回值
    //buffer.appendAll(for(num <- arr if num < 0)yield num)
    buffer
  }
  //println(fArr(Array(1,3,-1,2,-5,0,1)))       //结果:ArrayBuffer(1, 3, 2, 1, 0, -1, -5)
}

object TestDemo11 extends App {
  //设置一个映射，其中包含你想要的一些装备，以及它们的价格
  // 然后根据这个映射构建另一个新映射，采用同一组键，但是价格上打9折。
  def discount(map: Map[String,Double]): Map[String,Double] ={
    for ((k,v) <-map) yield (k,v * 0.9)
  }
  //println(discount(Map("book" -> 12.5,"pen" -> 8.0,"schoolbag" -> 38.9)))
  //结果:Map(book -> 11.25, pen -> 7.2, schoolbag -> 35.01)
}

object TestDemo12 extends App {
  //编写一段WordCount函数，统计传入的字符串中单词的个数
  def wordCount(str:String): mutable.Map[String,Int] ={
    val res = str.split("\\s+")
    val map = new mutable.HashMap[String,Int]()
    for (word <- res){
      map(word) = map.getOrElse(word,0) + 1
    }
    map
  }
  //println(wordCount("shi si shi shi si bu shi shi shi si shi shi si shi bu shi shi si"))
  //结果:Map(si -> 5, shi -> 11, bu -> 2)
}

object TestDemo13 extends App {
  //编写一个函数 minmax(values:Array[Int]), 返回数组中最小值和最大值的对偶
  def minmax(arr : Array[Int]): (Int,Int) ={
    (arr.min,arr.max)
  }
  //println(minmax(Array(1,3,5,4,12,0)))      //结果:(0,12)
}

object TestDemo14 extends App {
  //编写一个函数indexes，给定字符串，产出一个包含所有字符下标的映射
  // 举例来说：indexes(“Mississippi”)应返回一个映射，让’M’对应集{0}，‘i’对应集{1，4，7，10}，依次类推
  // 使用字符到可变集的映射，注意下标的集应该是有序的
  def indexes(str : String)={
    val map = new mutable.HashMap[Char,mutable.SortedSet[Int]]()
    var i:Int = 0
    for (char <- str){
       map.get(char) match {
         case Some(res) => map(char) = res + i
         case None => map += (char -> mutable.SortedSet{i})
       }
      i += 1
    }
    map
  }
  //println(indexes("Mississippi"))
  //结果:
  // Map(M -> TreeSet(0), s -> TreeSet(2, 3, 5, 6), p -> TreeSet(8, 9), i -> TreeSet(1, 4, 7, 10))
}

object TestDemo15 extends App {
  //编写一个函数，从一个整型链表中去除所有的零值。
  def fun(list: List[Int])={
    /*for (num <- list if num != 0){
      println(num)
    }*/
    val list1 = list.filter((x:Int) => x != 0)
    list1
  }
  println(fun(List(1,0,3,5,0,2)))
  //println(fun(List(1,3,0,2,7,0,12,3,0,6)))   //结果:List(1, 3, 2, 7, 12, 3, 6)
}

object TestDemo16 extends App {
  //实现一个函数，作用与mkStirng相同，提示：使用reduceLeft实现试试
  //mkStirng将多个字符组拼接为一个新的string
  def mkstr(arr:Array[String]): String ={
    val str = arr.reduceLeft(_+_)
    str
  }
  //println(mkstr(Array("无","奋斗",",","不","青春")))    //结果: 无奋斗,不青春
}

object TestDemo17 extends App {
  //利用模式匹配，编写一个swap函数，接受一个整数的对偶，返回对偶的两个组成部件互换位置的新对偶
  def swap(x:(Int,Int))={
    var res = x match {
      case (a,b) => (b,a)
      case _ =>
    }
    res
  }
  //println(swap((1,2)))      //结果: (2,1)
}

object TestDemo18 extends App {
  //利用模式匹配，编写一个swap函数，交换数组中的前两个元素的位置，前提条件是数组长度至少为2
  def swap(arr:Array[Int])={
    val arr1 = arr match {
      case Array(first,second,other@_*) => Array(second,first) ++ other
      case _ => arr
    }
    arr1
  }
  //println(swap(Array(1,3,2,4,8)).toList)             //结果:List(3, 1, 2, 4, 8)
}

object TestDemo19 extends App {}

object TestDemo20 extends App {}

object TestDemo21 extends App {}

object TestDemo22 extends App {}

object TestDemo23 extends App {}

object TestDemo24 extends App {}

object TestDemo25 extends App {}

object TestDemo26 extends App {}

object TestDemo27 extends App {}

object TestDemo28 extends App {}

object TestDemo29 extends App {}

object TestDemo30 extends App {}
