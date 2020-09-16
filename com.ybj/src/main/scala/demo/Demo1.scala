package demo

object Demo1 {
  def main(args: Array[String]): Unit = {
    //val修饰的变量不能再次赋值
    val num = 12.3
    val ages = 12.35

    //var修饰可变变量
    //scala根据变量声明时的赋值来自动推断类型,类型也是不可变的
    var names = "user"


    //scala类型判断
    val bool = ages.isInstanceOf[Double]

    //scala类型转换
    val nums = ages.asInstanceOf[Int]
    println(num+"*****"+names+"^^^^^"+bool+"!!!!!"+nums)

    //scala函数的声明
    /**
     *  def 函数名(参数名1:参数类型,参数名2:参数类型)[:返回值类型=]{
     *  函数体
     * 函数体中的最后一行代码作为函数的返回值
     * }
     */
    def f(age:Int,name:String,sex:Char): Unit ={
      println("我的名字叫"+name+",今年呢"+age+"岁了,我是一个活泼的小"+sex+"孩!!!")
    }
    // 如果在声明函数时不写返回值类型,那么默认使用Unit作为返回值
    // 如果需要让编译器根据最后一行的类型进行返回,可以在函数声明中，返回值位置上只写上 =
    def f2()={
      println("f2")
      1
    }
    def f3(): Unit ={
      println("f2")
      1
    }
    //调用f方法
    f(12,"帽子",'男')
    println(f2())//输出结果为 f2 1
    f2()//输出结果为 f2
    println(f3())//输出结果为 f2 ()
  }
}
