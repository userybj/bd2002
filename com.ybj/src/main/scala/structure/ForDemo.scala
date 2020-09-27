package structure

import scala.util.control.Breaks

object ForDemo {
  def main(args: Array[String]): Unit = {
    /**
     * for( 循环变量 <- 集合 ){
     * 每次从集合中拿到一个元素，进行遍历
     * }
     */
    val array = Array(1,2,3,4)
    for (num <- array){
      println(num)
    }
    // Range 区间 是scala中的一种集合
    // 通过一些函数快速生成range作为循环集合
    // 0 to 10 产生一个0-10的集合
    // 0 until 10 产生一个0-9的集合
    for (i <- 0 to 3){
      println(i)
    }
    for (a <- 0 until 4){
      println(a)
    }

    //break
    Breaks.breakable(
      for (b <- 2 to 6){
        if (b == 3)Breaks.break()
        println(b)
      }
    )

    //continue
    /**
     * scala for循环的循环条件末尾可以加上守卫
     * if boolean表达式
     * 表达式为True 执行循环
     */
    for (nums <- 0 to 10 if nums%2 ==0){
      println(nums)
    }

    // yield关键字可以将循环变量收集到集合中
    val ints = for (numb <- 0 to 10 if numb % 2 == 0)yield numb
    println(ints)

    // 过滤函数  算子 compute factor
    val ints1 = (0 to 10)filter(_ % 2 == 0)
    println(ints1)
  }
}
