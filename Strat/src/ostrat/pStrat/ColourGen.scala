/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pStrat
import geom._
import pCanv._

case class ColourGen(canv: CanvasPlatform) extends pCanv.CanvasSimple("Colour Generator")
{
  var line = 0
  val n = 2
  
  def intMaker(i: Int): Int =
  { val fac = i.toDouble / n.toDouble
   (255 * fac).toInt
  }
  
  val cols: Seq[(PolyFillDraw, TextGraphic)] = for {
    r <- 0 to n
    g <- 0 to n
    b <- 0 to n
    r1 = intMaker(r)
    g1 = intMaker(g)
    b1 = intMaker(b)
    c1 = Colour.fromInts(r1, g1, b1)
    c2 = Rectangle.colouredBordered(25, c1, 2).slate(left + 30, top - 20)
    c3 = TextGraphic(commaedInts(r1, g1, b1), 15, left + 120 vv top - 20)
  } yield (c2, c3)
   
  val cols2 = cols.iFlatMap[CanvO]{ (pair , i) =>
    val offset = - 35 * i
    Arr(pair._1.slateY(offset), pair._2.slateY(offset))
  }
  repaint(cols2)
}