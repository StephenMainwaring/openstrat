/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package geom
import Colour.Black

case class PolySubj(cen: Vec2, poly: Polygon, evObj: AnyRef, elems: Arr[PaintElem]) extends GraphicSubject with PolyActiveTr
{
  def fTrans(f: Vec2 => Vec2): PolySubj = new PolySubj(f(cen), poly.fTrans(f), evObj, elems.trans(f))
  override def addElems(newElems: Arr[PaintElem]): PolySubj = new PolySubj(cen, poly, evObj, elems ++ newElems)
  override def mutObj(newObj: AnyRef): PolySubj = new PolySubj(cen, poly, newObj, elems)
}

object PolySubj
{
  def fill(cen: Vec2, poly: Polygon, evObj: AnyRef, colour: Colour) = new PolySubj(cen, poly, evObj, Arr(poly.fill(colour)))
   
  /** Not sure if this is double filling the polygon */
  def fillDraw(cen: Vec2, poly: Polygon, evObj: AnyRef, fillColour: Colour, lineWidth: Double, lineColour: Colour = Black) =
    new PolySubj(cen, poly, evObj, Arr(PolyFillDraw(poly, fillColour, lineWidth, lineColour)))
   
  def draw(cen: Vec2, poly: Polygon, evObj: AnyRef, lineWidth: Double, lineColour: Colour = Black) =
      new PolySubj(cen, poly, evObj, Arr(PolyDraw(poly, lineWidth, lineColour)))
  
  def fillText(cen: Vec2, poly: Polygon, evObj: AnyRef, fillColour: Colour, str: String, fontSize: Int = 4, fontColour: Colour = Colour.Black,
      align: TextAlign = CenAlign) =
        new PolySubj(cen, poly, evObj, Arr(poly.fill(fillColour), TextGraphic(str, fontSize, poly.polyCentre, fontColour, align)))
  
  def fillContrastText(cen: Vec2, poly: Polygon, evObj: AnyRef, fillColour: Colour, str: String, fontSize: Int = 4) =
    fillText(cen, poly, evObj, fillColour, str, fontSize, fillColour.contrast)
}

