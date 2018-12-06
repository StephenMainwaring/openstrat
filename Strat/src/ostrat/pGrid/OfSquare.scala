/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pGrid
import geom._

trait OfSquare[TileT <: GridElem, SideT <: GridElem, GridT <: SquareGrid[TileT, SideT]] extends OfTile[TileT, SideT, GridT]
{
   def sideUpLine: Line2 = CoodLine(cood.addXY(-1, 1), cood.addXY(1, 1)).toLine2(cood => coodToDispVec2(cood))
   def sideRightLine: Line2 = CoodLine(cood.addXY(1, 1), cood.addXY(1, - 1)).toLine2(cood => coodToDispVec2(cood))
   override def ownSideLines: List[Line2] = List(sideUpLine, sideRightLine)
}

case class OfSquareReg[TileT <: GridElem, SideT <: GridElem, GridT <: SquareGrid[TileT, SideT]](
      tile: TileT, grid: GridT, gGui: TileGridGui[TileT, SideT, GridT]) extends OfSquare[TileT, SideT, GridT] with OfTileReg[TileT, SideT, GridT]

object OfSquareReg
{
   implicit def implicitBuilder[TileT <: GridElem, SideT <: GridElem, GridT <: SquareGrid[TileT, SideT]]
   (tile: TileT, grid: GridT, gGui: TileGridGui[TileT, SideT, GridT]): OfSquareReg[TileT, SideT, GridT] = apply(tile, grid, gGui)    
}