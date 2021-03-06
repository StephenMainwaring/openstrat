/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package geom

/** Factory object for squares. There is no companion Square class. */
object Square extends UnScaledPolygon
{
  override val scaled: Polygon = Polygon(0.5 vv 0.5, 0.5 vv -0.5, -0.5 vv -0.5, -0.5 vv 0.5)
  def apply(width: Double = 1, cen: Vec2 = Vec2Z): Polygon = scaled.fTrans(_ * width + cen)
  def xy(width: Double, xCen: Double, yCen: Double): Polygon = Polygon(
      xCen - width / 2 vv yCen + width / 2,
      xCen + width / 2 vv yCen + width / 2,
      xCen + width / 2 vv yCen - width / 2,
      xCen - width/2   vv yCen - width / 2)
   
  /**Needs Changing possibly removing. */
  def fill(width: Double, colour: Colour, cen: Vec2 = Vec2Z): PolyFill = apply(width, cen).fill(colour)
  /**Needs Changing possibly removing. */
  def fillXY(width: Double, colour: Colour, xCen: Double, yCen: Double): PolyFill = apply(width, xCen vv yCen).fill(colour)
   
  def curvedSegs(width: Double, radius: Double): List[CurveSeg] =
  { val w = width / 2
    (0 to 3).toList.flatMap(i => List( LineSeg(w - radius, w), ArcSeg(w - radius vv w -radius, w vv w - radius)).rotateRadians(-i * math.Pi / 2))
  }
}