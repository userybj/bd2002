package structure

object MatchDemo {
  def main(args: Array[String]): Unit = {
    var sign = 0
    var ch = '+'
    ch match {
      case '+' => sign = 1
      case '-' => sign = 2
      case _ => sign = 3
    }
    //println(sign)

    for (ch <- "+-3!"){
      var sign = 0
      ch match {
        case '+' => sign = 11
        case '-' => sign = 22
        case _ if Character.isDigit(ch) => sign = Character.digit(ch,10)
        case _ => sign = 0
      }
      //println(ch + "*******" + sign)
      //结果
      /**
       * +*******11
       * -*******22
       * 3*******3
       * !*******0
       */
    }

    val strs = "+-5!"
    for (i <- strs.indices){
      var sign = 0
      strs(i) match {
        case '+' => sign = 333
        case '-' => sign = 222
        // case中可以声明一些形参
        // case语句会将参与match的对象赋值给这些参数
        // chs = str(i)
        case chs if Character.isDigit(chs) => sign = Character.digit(chs,10)
        case _ => sign = 666
      }
      //println(strs(i) + "*******" + sign)
      //结果
      /**
       * +*******333
       * -*******222
       * 5*******5
       * !*******666
       */
    }

    //match进行数据类型的判断
    val i:Any = 2
    var result = i match {
      case x : Int => x
      case z : Double => z.toString
      case _ : BigInt => Int.MaxValue
      case BigInt =>
    }
    //println(result)   //输出:2
    val map : Any = Map[String,Int]("大号" -> 1)
    //!!!!模式匹配中  多泛型的对象，泛型不参与匹配
    var result1 = map match {
      case n : Map[Double,Int] => "啥也不是"
      case n : Map[String,Int] => "这是map集合"
      case _ : Map[_,_] => "你啥也不是"
      case _ => "你也啥也不是"
    }
    //println(result1)    //输出:啥也不是

    for (arr <- Array(
      Array(0),
      Array(1, 0),
      Array(6, 8),
      Array(0, 1, 0),
      Array(1, 1, 0)
    )) {
      val result = arr match {
        //        case Array(0) => "0"
        case Array(x, y) => x + " " + y
        case Array(0, a@_*) => "0 ..." + a
        case _ => "something else"
      }
    }


    for (lst <- Array(
      List(0),
      List(1, 0),
      List(0, 0, 0),
      List(1, 0, 0))) {
      val result = lst match {
        case 0 :: Nil => "0"
        case x :: y :: z :: Nil => x + " " + y
        case 0 :: tail => "0 ..." + tail
        case _ => "something else"
      }
    }

    for (pair <- Array(
      (0, 1),
      (1, 0),
      (1, 1))) {
      val result = pair match {
        case (0, _) => "0 ..."
        case (y, 0) => y + " 0"
        case _ => "neither is 0"
      }
    }

    for (persion <- List(Persions("Tom",12),Persions("Jack",8))){
      var result = persion match {
        case Persions(name, 12) => name
        case persion => persion.name
      }
      //println(result)
      //输出结果
      //Tom
      //Jack
    }

  }
  case class Persions(name : String,age : Int)
}
