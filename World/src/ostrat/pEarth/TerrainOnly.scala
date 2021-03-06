/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pEarth
import geom._, pGrid._

case class TerrOnly(x: Int, y: Int, terr: WTile) extends ETile
{
  type FromT = WTile
  def fromT = terr
}
object TerrOnly
{
   implicit val tileMaker: (Int, Int, WTile) => TerrOnly = apply
   implicit object TerrOnlyIsType extends IsType[TerrOnly]
   {
      override def isType(obj: AnyRef): Boolean = obj.isInstanceOf[TerrOnly]
      override def asType(obj: AnyRef): TerrOnly = obj.asInstanceOf[TerrOnly]
   }
}

case class SideOnly(x: Int, y: Int) extends TileSide

object SideOnly
{
   implicit object TerrOnlyIsType extends IsType[SideOnly]
   {
      override def isType(obj: AnyRef): Boolean = obj.isInstanceOf[SideOnly]
      override def asType(obj: AnyRef): SideOnly = obj.asInstanceOf[SideOnly]
   }
}

class EGridOnly(name: String, cenLong: Longitude, scale: Dist, xOffset: Int, yOffset: Int,  xTileMin: Int, xTileMax: Int,
    yTileMin: Int, yTileMax: Int, turnNum: Int) extends EGrid[TerrOnly, SideOnly](new Array[Int](0), name, cenLong, scale, xOffset, yOffset,
    xTileMin, xTileMax, yTileMin, yTileMax, turnNum)
          