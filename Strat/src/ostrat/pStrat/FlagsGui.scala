/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pStrat
import geom._, pCanv._, Colour._, pFlags._, FlagsZ._

case class FlagsGui(canv: CanvasPlatform) extends CanvasSimple("Flags Gui")
{
  backColour = Gray
   
  val arm = Armenia().scale(100)//.slate(canv.topLeft)
   
  val stuff = arm ++ List(
      france.scale(100).tR.slate(canv.topRight),
      chad.scale(100).tL.slate(canv.topLeft),
      // belgium.scale(100).bL.slate(canv.bottomLeft),
         
      belgium.scale(100).slate(500, -100).rotate(Angle(math.Pi / 7)),
      us.scale(150).slate(400, 350),
      uk.scale(200).slate(300, -350).rotate(10.degs),
      japan.scale(200).slate(650, 200).rotate(10.degs),
   )
               
  mouseUp = (v, b, s) =>
    {
      val str: String = s.headOption.map(_.toString).getOrElse("No clickable object on canvas")
      val tg = TextGraphic(str, 28, 0 vv 100)
      repaint(stuff :+  tg)
    }
  repaint(stuff)  
}  
