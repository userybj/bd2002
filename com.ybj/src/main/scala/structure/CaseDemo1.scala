package structure

object CaseDemo1 {
  def main(args: Array[String]): Unit = {
    val person3 = Person3("Tom",8)
    //println(person3)     //结果:Person3(Tom,8)
    var person4 = person3
    // 由于对象为引用类型如果直接传参会修改原来对象的属性
    // 我们如果想不改变原来的对象需要对对象进行克隆
    // new 一个新的对象，将原来对象所有的属性赋值给新对象
    // 样例类中的copy方法可以完成这个操作
    // copy时可以重新给属性赋值
    val person5 = person3.copy(name = "Jack")
    person4.name = "Aike"
    //println(person3)       //结果:Person3(Aike,8)


    for (amt <- Array[Amount](Dollar(1000.0),Currency(1000.0, "EUR"),Nothing)) {
      val result = amt match {
        case Dollar(v) => "$" + v
        case Currency(_, u) => "Oh noes, I got " + u
        case Nothing => ""
      }
      // Note that amt is printed nicely, thanks to the generated toString
      println(amt + ": " + result)
      //结果
      /**
       * Dollar(1000.0): $1000.0
       * Currency(1000.0,EUR): Oh noes, I got EUR
       * Nothing:
       */
    }
  }



  // 1. 主构造器中写的参数，默认使用val修饰(默认认为这些参数是类的不可变属性)
  case class Person1(name: String)
  //可以认为对val进行修改
  case class Person2(var name: String)

  // 2. 样例类编译时会自动编译出伴生对象，并实现apply方法,默认是全参的apply
  case class Person3(var name: String, age: Int)

  // 3. 样例类编译时会自动编译出伴生对象，并实现unapply方法，用于match中提取属性
  // 4. 样例类会自动重写toString
  // 5. 样例类会自动实现copy方法



  abstract class Amount
  case class Dollar(value: Double) extends Amount
  case class Currency(value: Double, unit: String) extends Amount

  case object Nothing extends Amount
}
