package structure

object FunctionDemo1 {
  def main(args: Array[String]): Unit = {
    //函数返回值问题
    /**
     * 1. 通常情况下，声明函数时如果是def声明 函数体前写上 =
     * scala会自动按照函数体的最后一行进行返回值的类型推断
     * 如果=也不写，相当于Java void
     */
    val f1: () => Unit = () => {}
    def f2(a:Int,b:Int)={
      a + b
    }
    val f3: (Int, Int) => Int = (a:Int,b:Int) => a + b
    /**
     * 2.根据最后一行进行返回时，可以不用写return关键字
     * 在递归函数中，必须明确声明返回值类型
     * 因为递归函数在编译时期并不知道需要调用多少次
     * 也不知道调用之后的结果会不会返回相同的类型
     */
      //阶乘
    def factorial(num:Int): Int={
        //递归的结束条件
        if (num < 2) num
        //递归调用
        else num * factorial(num - 1)
    }

    // 高阶函数的特性
    // 1.把函数作为其他函数的参数
    def f4(a:Int,b:String,f:(String,Int) => String): Unit ={
      f(b,a)
    }
    f4(3,"6",_ * _)
    // 2.柯里化
    /**所有的函数 都可以写成只有一个参数的形式
    *如果要将多个参数的函数 改写成单参函数
    *可以将后面的逻辑以函数的形式作为单参函数的返回值
    */
    def f5(a:Int,b:Int)={
      a + b
    }
    def f6(a:Int)={
      b:Int => a + b
    }
    //调用
    f6(3)(7)
    // 3.把函数作为其他函数的返回值
    def f7(a:Int,b:Int,f:(Int,Int) => Int)={
      f(a,b)
    }
    def f8(a:Int,b:Int)={
      (f:(Int,Int) => Int) => f(a,b)
    }
    //调用
    f8(3,5)(_ * _)

    //lazy修饰的属性
    //在第一次调用时才进行初始化
    lazy val str = "abc"

    val i = 1

    val str1 = "xyz"
    try {
        3/0

        val a:String = null
        a.length

        val ints:Array[Int] = Array(1, 2)
        val i1 = ints(2)

    }catch {
        //case 形参:异常类型  => {
        // 异常的处理逻辑
        //}
        case e:ArithmeticException => println("算数异常")
        case e:NullPointerException =>println("空指针")
        case e:ArrayIndexOutOfBoundsException =>println("下标越界")
        case e:Exception => println("未知异常")
    }finally {
    }
  }
}
