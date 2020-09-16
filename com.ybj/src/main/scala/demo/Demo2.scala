package demo

object Demo2 {
  def main(args: Array[String]): Unit = {
    new Persion("大毛")
    new Persion("大毛",13).study("😊")
    new Persion(" ").study("😴")
  }
}

/**
 * 在scala中类的声明中，类名后面的小括号叫做 主构造器，主构造器只有一个
 * 在主构造器的参数列表中，声明的带有val/var修饰的变量将作为类的属性
 */
class Persion private(){
  var name : String = _
  var age : Int =_
  //scala的class中还可以声明若干个辅助构造器,类似java中构造器的重载
  //所有的辅助构造器都必须直接或者间接的调用主构造器,辅助构造器就是一些名字为this的函数
  def this(name:String){
    this()//主构造器
    this.name = name
  }
  def this(name:String,age:Int){
    this(name)//间接的调用主构造器
    this.age = age
  }
  def study(str:String): Unit ={
    println("一学习就"+str)
  }
}

//样例类
case class Animal(name:String)
//在样例类中,主构造器中的参数,默认使用val修饰