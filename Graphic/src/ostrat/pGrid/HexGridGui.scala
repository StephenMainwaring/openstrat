/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pGrid
import geom._, pCanv._

/** Class for displaying a single hex grid */
abstract class HexGridGui[TileT <: Tile, SideT <: TileSide, GridT <: HexGridReg[TileT, SideT]](val canv: CanvasPlatform, title: String) extends
   TileGridGui[TileT, SideT, GridT](title)
{   
   def ofHTilesFold[R](f: OfHexReg[TileT, SideT, GridT] => R, fSum: (R, R) => R, emptyVal: R) =
   {
      var acc: R = emptyVal
      foreachTilesCoodAll{ tileCood =>
         val newTog = OfHexReg(grid.getTile(tileCood), grid, this)
         val newRes: R = f(newTog)
         acc = fSum(acc, newRes)
      }
      acc
   }
   
   def ofHTilesDisplayFold(f: OfHexReg[TileT, SideT, GridT] => GraphicElems): GraphicElems = ofHTilesFold[GraphicElems](f, _ ++ _, Arr())
//   def ofHexsFold[R](f: RegHexOfGrid[TileT] => R, fSum: (R, R) => R, emptyVal: R) =
//   {
//      var acc: R = emptyVal
//      grid.foreachTileCood{ tileCood =>         
//         val newRes: R = f(RegHexOfGrid[TileT](this, grid, grid.getTile(tileCood)))//{val grid: TileGrid[TileT, SideT]=  thisGrid })
//         acc = fSum(acc, newRes)
//      }
//      acc
//   }
//   
//   def ofHexsDisplayFold(f: RegHexOfGrid[TileT] => Disp2): Disp2 = ofHexsFold[Disp2](f, _ ++ _, Disp2.empty)  
   //override def sideXYVertCoods(x: Int, y: Int): CoodLine = HexGrid.sideXYVertCoods(x, y)
   //override val yRatio: Double = HexCood.yRatio
//   val xRadius: Double = 2 
//   val yRadius: Double = HexCood.yDist2
// 
   //val scaleMin = gridScale / 1000
   //val scaleMax: Dist = gridScale / 10
  // var scale = scaleMax
//   mapFocus = mapCen  
}

//abstract class HexRegGui[TileT <: Tile, SideT <: TileSide](val canv: CanvasPlatform, val grid: HexGridComplexReg[TileT, SideT]) extends GridGui[TileT, SideT]