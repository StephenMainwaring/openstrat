/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package learn
import ostrat._, geom._, pCanv._, Colour._

case class LessonC3(canv: CanvasPlatform) extends CanvasSimple("Lesson C3")
{
  val r = Rectangle(200, 100).fillSubj(None, Yellow)
  val r1 = r.slate(-300, 300)
  val r2 = r.slate(300 vv 300)
  val r3 = r.slate(300 vv - 300)
  val r4 = r.slate(-300 vv - 300)
  val rList = List(r1, r2, r3, r4)
  val textPosn = 0 vv 0
  val startText = TextGraphic("Please click on the screen.", 28, textPosn)
  repaint(rList :+ startText)
  
  mouseUp = (v, b, s) =>
    {
      val newText = s match
      {
        case h :: tail => TextGraphic("You hit a yellow rectangle at " + v.commaStr, 28, textPosn)
        case _ => TextGraphic("You missed the yellow rectangles " + v.commaStr, 28, textPosn)
      }  
      repaint(rList :+ newText)
  }
}