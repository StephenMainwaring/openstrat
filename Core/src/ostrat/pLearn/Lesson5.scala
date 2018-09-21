/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pLearn
import geom._, pCanv._, Colour._

case class Lesson5(canv: CanvasPlatform) extends CanvasSimple
{
   def bd(c1: Vec2, c2: Vec2, colour: Colour) = BezierDraw(Vec2Z, c1, c2, 500 vv 350, 3, colour)
   //This can be more elegantly expressed in dotty
   def fun(a: Int, b: String, c: Double, d: Boolean): Int = a + b.length + c.toInt + (if (d) 1 else 0)
   val pt1 = 500 vv - 400
   val sh1 = Shape(LineSeg(Vec2Z), LineSeg(200 vv 0), BezierSeg(300 vv 300, 350 vv 100, pt1), LineSeg(100 vv -200)) 
   def stuff = List(        
         bd(-100 vv 200, 300 vv 400, Green),
         bd(-150 vv -50, 250 vv 350, Violet),
         bd(-250 vv 50, 200 vv 400, Orange),
         bd(-300 vv 100, 200 vv 0, Pink),
         ShapeFill(sh1, Yellow), 
         TextGraphic(pt1, pt1.toString, 12),
         )
//   def stuff2(obj: Any) = stuff :+  TextGraphic.xy(0, 0, obj.toString, 20, Colour.Turquoise)
//   mouseUp = (v, b, s) =>   { repaint(stuff2(s.headOrElse("No clickable object on canvas"))); canv.bezierDraw(bd) }
   
   repaint(stuff)//2("Begin"))
   
}