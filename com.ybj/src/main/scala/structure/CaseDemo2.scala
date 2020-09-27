package structure

object CaseDemo2 {
  def main(args: Array[String]): Unit = {
    val bundle = Bundle("Father's day special",20.0,
      Article("Scala for the Impatient", 39.95),
      Bundle("Anchor Distillery Sampler",10.0,
        Article("Old Potrero Straight Rye Whiskey", 79.95),
        Article("Junípero Gin", 32.95)))

    //找出10.0
    val discount = bundle.items.toList(1).asInstanceOf[Bundle].discount
    //println(discount)
    bundle match {
      case Bundle(_, _, _,Bundle(_,discount,_*),_*) =>  //println(discount)
    }

    //计算价格(总价-折扣)
    def totalPrice(item: Item):Double={
      item match {
        case Article(description, price) => price
        case Bundle(description, discount, items@_*) => items.map(totalPrice).sum - discount
      }
    }
    val total = totalPrice(bundle)
    //println(total)

    //九九乘法表
    /*for (i <- 1 to 9){
      for (j <- 1 to i){
        print(i+"*"+j+"="+i*j+"\t")
      }
      println()
    }*/

    //0~100以内的质数
    /*var judge = true
    for (num <- 2 to 100){
      judge = true
      var i = 2
      for (i <- 2 until num  if judge != false){
        if(num % i == 0) judge = false
      }
      if (judge == true) println(num)
    }*/



  }
  //             商品
  abstract class Item

  //          选集    描述  ：字符串        价格： 浮点
  case class Article(description: String, price: Double) extends Item

  //          套餐    描述                 折现              包含的商品集
  case class Bundle(description: String, discount: Double, items: Item*) extends Item
}
