package demo

/**
 * 如果一个object和一个class名字完全相同并且声明在同一个scala文件中
 * 那么我们就把这个object叫做这个class的伴生对象,把这个class叫做这个object的伴生类
 *
 * 如果要执行类似Java里面静态或者写工具类之类的操作
 * 有一个关键字叫object,object声明的对象本身就是一个对象实例,就是一个单例对象
 */
//scala中类就是对象的【模板】
//必须实例化才能调用属性和方法
//伴生类
class Student {
  var name:String = _
  var age:Int = _
  def eat(): Unit ={
    println("正在吃东西。。。")
  }
  def sleep(time:Int): Unit ={
    println("每天就睡"+time+"小时，😴")
  }
}

/**
 * 类Student的伴生对象
 * 伴生类和伴生对象要在同一个源文件中定义，伴生对象和伴生类可以互相访问其私有成员
 */
object Student{
  def work(time:Int): Unit ={
    println("每天要工作"+time+"小时，累啊")
  }
  //在伴生对象中有一类特殊方法叫做apply，这种方法可以直接使用 对象名(参数)进行调用
  //apply通常用作扩充伴生类的构造
  def apply(): String = {
    "敌杀死"
  }

  def apply(string: String): String={
    string
  }
  //apply扩充一个传递name和age的构造
  def apply(name:String,age:Int): Student = {
    val stu = new Student
    stu.name = name
    stu.age = age
    stu
  }
}
object StudentTest{
  def main(args: Array[String]): Unit = {
    val str = Student.apply()
    val str1 = Student.apply("*敌杀死*")
    Student.apply("好嗨哟",18).eat()
    Student.apply("我是谁",13).sleep(3)
    Student.work(25)
    println(str+str1)
    var num = 3.1415
    val numb = num.intValue()
    println(numb)
  }
}
