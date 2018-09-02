/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package geom
import Colour.Black

trait ArcSegLike extends CurveSegLike
{
   def xCen: Double
   def yCen: Double
   def pCen: Vec2 = Vec2(xCen, yCen)
   def radius: Double = (pEnd - pCen).magnitude
}

trait ArcLike extends ArcSegLike with CurveLike
{   
   def startAngle: Angle = (pStart - pCen).angle
   def endAngle: Angle = (pEnd - pCen).angle
   def deltaAngle: Angle = startAngle.angleTo(endAngle)
   def controlPt: Vec2 = 
   {
      val sAng: Angle = startAngle     
      val resultAngle = sAng.bisect(endAngle)
      val alphaAngle =  sAng.angleTo(endAngle) / 2      
      pCen + resultAngle.toVec2 * radius / alphaAngle.cos
   }
   /** Calculates ControlPt and then passes controlPt.x, controlPt.y, XENd, yEnd, radius to f */
   def fControlEndRadius(f: (Double, Double, Double, Double, Double) => Unit): Unit =
   {
      val cp = controlPt
      f(cp.x, cp.y, xEnd, yEnd, radius)
   }
}

case class ArcAlt(xStart: Double, yStart: Double, xCen: Double, yCen: Double, xEnd: Double, yEnd: Double) extends CurveLike with ArcLike
{   
   def persistName = "Arc"
   override def toString = ???// namedStr2
   def fTrans(f: Vec2 => Vec2): ArcAlt = ArcAlt(f(pStart), f(pCen), f(pEnd))   
}

object ArcAlt
{
   def apply(pStart: Vec2, pCen: Vec2, pEnd: Vec2): ArcAlt =  new ArcAlt(pStart.x, pStart.y, pCen.x, pCen.y, pEnd.x, pEnd.y)
}

/** Takes its start point from the previous segment */
case class ArcSeg(xCen: Double, yCen: Double, xEnd: Double, yEnd: Double) extends CurveSeg
{
   def pCen: Vec2 = Vec2(xCen, yCen)
   def fTrans(f: Vec2 => Vec2): CurveSeg = ArcSeg(f(pCen),f(pEnd))
   def startAngle(pStart: Vec2): Angle = (pStart - pCen).angle
   def endAngle: Angle = (pEnd - pCen).angle
   def radius: Double = (pEnd - pCen).magnitude
   def controlPt(startPt: Vec2): Vec2 = 
   {
      val sAng: Angle = startAngle(startPt)      
      val resultAngle = sAng.bisect(endAngle)
      val alphaAngle =  sAng.angleTo(endAngle) / 2      
      pCen + resultAngle.toVec2 * radius / alphaAngle.cos
   }
   
   /** Calculates ControlPt and then passes controlPt.x, controlPt.y, XENd, yEnd, radius to f */
   def fControlEndRadius(startPt: Vec2, f: (Double, Double, Double, Double, Double) => Unit): Unit =
   {
      val cp = controlPt(startPt)
      f(cp.x, cp.y, pEnd.x, pEnd.y, radius)
}
}

object ArcSeg
{
   def apply(pCen: Vec2, pEnd: Vec2): ArcSeg = new ArcSeg(pCen.x, pCen.y, pEnd.x, pEnd.y)
}

case class ArcDraw(xStart: Double, yStart: Double, xCen: Double, yCen: Double, xEnd: Double, yEnd: Double, lineWidth: Double,
      colour: Colour) extends PaintElem[ArcDraw] with ArcLike
{
   override def fTrans(f: Vec2 => Vec2) = ArcDraw(f(pStart), f(pCen), f(pEnd), lineWidth, colour)   
}

object ArcDraw
{
   def apply(pStart: Vec2, pCen: Vec2, pEnd: Vec2, lineWidth: Double = 1.0, colour: Colour = Black): ArcDraw =
      new ArcDraw(pStart.x, pStart.y, pCen.x, pCen.y, pEnd.x, pEnd.y, lineWidth, colour)
}
