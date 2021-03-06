/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package geom

/** The base trait for all objects that can have mouse / touch pad interaction. */
trait GraphicActive
{ /** If the user clicks with the polygon or shape then the canvas will return this object. It is purely up to the application its
   *  response if any to this object */
  def evObj: AnyRef
  /** The bounding Rectangle provides an initial exclusion test as to whether the pointer is inside the polygon / shape */
  def boundingRect: BoundingRect
  /** The definitive test as to whether the mouse pointer is inside the polygon / shape */
  def ptInside(pt: Vec2): Boolean
}

object GraphicActive
{
  implicit class GraphicActiveListImplicit(thisArr: Arr[GraphicActive])
  { /** Note the lack of reverse at the end */
    def ptInList(pt: Vec2): Arr[AnyRef] = thisArr.filter(subj => subj.boundingRect.ptInside(pt) & subj.ptInside(pt)).map(_.evObj)
  }
}

/** An active transparent pointable polygon */
trait PolyActiveTr extends GraphicActive
{ def poly: Polygon
  override def boundingRect = poly.boundingRect
  override def ptInside(pt: Vec2): Boolean = poly.ptInPolygon(pt)
}

/** A pointable shape */
trait ShapeActiveTr extends GraphicActive
{ def shape: Shape
  def innerPoly: Polygon = shape.pMap(_.pEnd)
  def boundingRect: BoundingRect = innerPoly.boundingRect
  /** This method needs improving */
  override def ptInside(pt: Vec2): Boolean = innerPoly.ptInPolygon(pt)
}

/** A pointable shape without visual */
case class ShapeActive(shape: Shape, evObj: AnyRef, zOrder: Int = 0) extends GraphicElem with ShapeActiveTr
{ override def fTrans(f: Vec2 => Vec2) = ShapeActive(shape.fTrans(f), evObj) }
