/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
import geom._, pGrid._

package object pEarth
{
   /** The North-South divide between Area1s and Grids at 45 degrees north approx. */
   val divN45 = 45.27369792435918.north
   //import HexGrid._
   /** Returns a function for a specific EGrid to convert from gridVec to Latlong */
   def fVec2ToLatLongReg(refLong: Longitude, scale: Dist, xOffset: Int, yOffset: Int = 0): Vec2 => LatLong = inp =>
      {
         val vOffset: Vec2 = HexGrid.coodToVec2(xOffset, yOffset)
         val d2: Dist2 = (inp - vOffset) * scale
         val lat: Double = d2.y / EarthPolarRadius         
         val longDelta: Double =   d2.x / (EarthEquatorialRadius * math.cos(lat))
         LatLong(lat, refLong.radians + longDelta)
      }
      
   def vec2ToLatLongReg(inp: Vec2, refLong: Longitude, scale: Dist, xOffset: Int, yOffset: Int = 0): LatLong =
      {
         val vOffset: Vec2 = HexGrid.coodToVec2(xOffset, yOffset)
         val d2: Dist2 = (inp - vOffset) * scale
         val lat: Double = d2.y / EarthPolarRadius         
         val longDelta: Double =   d2.x / (EarthEquatorialRadius * math.cos(lat))
         LatLong(lat, refLong.radians + longDelta)
      }   
   
   /** Not necessarily used */   
   def vec2ToLatLong0(inp: Vec2, refLong: Longitude, scale: Dist, yOffset: Int = 0): LatLong =
   {
      val vOffset: Vec2 = HexGrid.coodToVec2(0, yOffset)
      val d2: Dist2 = (inp - vOffset) * scale
      val lat: Double = d2.y / EarthPolarRadius         
      val longDelta: Double =   d2.x / (EarthEquatorialRadius * math.cos(lat))
      LatLong(lat, refLong.radians + longDelta)
   }
   
   /** Not necessarily used */
   def  coodToLatLong0(inp: Cood, scale: Dist, yOffset: Int = 0): LatLong =
   {
      val adj: Vec2 = HexGrid.coodToVec2(inp.subY(yOffset))      
      val d2: Dist2 = adj * scale
      val lat = d2.y / EarthPolarRadius         
      val longDelta: Double =   d2.x / (EarthEquatorialRadius * math.cos(lat))
      LatLong(lat, longDelta)
   } 
}