/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pCanv
import geom._
import Colour.Black

/** An abstract Canvas trait. A concrete implementation will utilise  canvas like an HTML canvas or a Scalafx canvas. This concrete implementation
 *  class must (can?) be mixed in with a a particular use trait like CanvSimple or CanvMulti .The default methods take the origin as the centre of the
 *  canvas. Note the Canvas Platform merely passes bare pointer event data to delegate functions. It does not process them in relation to objects
 *  painted on the Canvas. */
trait CanvasPlatform extends RectGeom
{
   /** The canvas implementation will call this function when a mouse button is released. Named after Javascript command */
   var mouseUp: (Vec2, MouseButton) => Unit = (v, b) => {}
   /** The canvas implementation will call this function when the mouse button is depressed. Named after Javascript command */
   var mouseDown: (Vec2, MouseButton) => Unit = (v, b) => {}
   var mouseMoved: (Vec2, MouseButton) => Unit = (v, b) => {}
   var mouseDragged: (Vec2, MouseButton) => Unit = (v, b) => {}
   var onScroll: Boolean => Unit = b => {}
   var resize: () => Unit = () => {}
   def clip(pts: Vec2s): Unit
   /** Returns the time in milliseconds */
   def getTime: Double
   /** A callback timer with an elapsed time from a given start point. The function is of form:
    *  (elapsedTime(in milliseconds), Startime(in millseconds) => Unit.
    *  The startTime is to be used to call the next frame at then end of the function, if another frame is needed */
   def frame(f: (Double, Double) => Unit, startTime: Double, frameLength: Integer = 15): Unit =
      timeOut(() => f(getTime - startTime, startTime), frameLength)   
   def frameZero(f: (Double, Double) => Unit, frameLength: Integer = 15): Unit = frame(f, getTime)
   /** A call back timer. Takes the delay in milliseconds */
   def timeOut(f: () => Unit, millis: Integer): Unit  
   var textMin: Int = 10
   
   final def polyFill(verts: Vec2s, colour: Colour): Unit = polyFill(PolyFill(verts, colour))
   def polyFill(pf: PolyFill): Unit
   final def drawPoly(lineWidth: Double, lineColour: Colour, pts: Vec2s): Unit = polyDraw(PolyDraw(pts, lineWidth, lineColour)) 
   def polyDraw(dp: PolyDraw): Unit

   def polyFillDraw(pfd: PolyFillDraw): Unit
   
   def polyDrawText(pts: Vec2s, lineWidth: Double, borderColour: Colour, str: String, fontSize: Int, fontColour: Colour = Black): Unit =
   {
      drawPoly(lineWidth, borderColour, pts: Vec2s)
      textGraphic(pts.polyCentre, str, fontSize, fontColour) 
   }
   
   def arcDraw(arc: Arc, lineWidth: Double, lineColour: Colour): Unit
   def bezierDraw(bd: BezierDraw): Unit 
   def linesDraw(lineSegs: Line2s, lineWidth: Double, linesColour: Colour): Unit
   def shapeFill(segs: List[ShapeSeg], fillColour: Colour): Unit
   def shapeFillDraw(segs: List[ShapeSeg], fillColour: Colour, lineWidth: Double, borderColour: Colour = Colour.Black): Unit
   def shapeDraw(segs: List[ShapeSeg], lineWidth: Double, borderColour: Colour = Colour.Black): Unit   
   def textGraphic(posn: Vec2, text: String, fontSize: Int, colour: Colour = Colour.Black, align: TextAlign = TextCen): Unit 
   def textOutline(posn: Vec2, text: String,  fontSize: Int, colour: Colour = Colour.Black): Unit
   
   def toBL(input: Vec2): Vec2 = Vec2(input.x, height - input.y)      
   
   def animSeq(anims: Seq[DispPhase]): Unit = anims match
   {
      case Seq() => 
      case Seq(DispStill(f), _*) => f()   
      case Seq(DispAnim(fAnim, secs), tail @ _*) =>
      {
         val start = getTime
         def func(): Unit =  
         {            
            val curr = getTime
            val elapsed = (curr - start) / 1000
            fAnim(elapsed)
            if (elapsed < secs) timeOut(() => func(), 30) else animSeq(tail)
         }         
         timeOut(() => func(), 30)
      }
   }

   def clear(colour: Colour = Colour.White): Unit   
   def gcSave(): Unit
   def gcRestore(): Unit 
   def saveFile(fileName: String, output: String): Unit
   def loadFile(fileName: String): EMon[String]
   def fromFileFind[A: Persist](fileName: String): EMon[A] = loadFile(fileName).findType
   def fromFileFindElse[A: Persist](fileName: String, elseValue: => A): A = fromFileFind(fileName).getElse(elseValue)
   /** Attempts to find find and load file, attempts to parse the file, attempts to find object of type A. If all stages successful, calls 
    *  procedure (Unit returning function) with that object of type A */
   def fromFileFindForeach[A: Persist](fileName: String, f: A => Unit): Unit = fromFileFind(fileName).foreach(f) 
    
   def rendElems(elems: List[PaintElem[_]]): Unit = elems.foreach(rendElem) 
   def rendElem(el: PaintElem[_]): Unit = el match
   {
      case fp: PolyFill => polyFill(fp)//verts, fillColour)
      case dp: PolyDraw => polyDraw(dp)// (verts, lineWidth, lineColour) => polyDraw(verts, lineWidth, lineColour)
      case pfd: PolyFillDraw => polyFillDraw(pfd)
      case LinesDraw(lines, lineWidth, lineColour) => linesDraw(lines, lineWidth, lineColour)
      //This might need reimplementing
      case LineDraw(line, lineWidth, lineColour) => linesDraw(Line2s(line), lineWidth, lineColour)
      case ShapeFill(segs, fillColour) => shapeFill(segs, fillColour)
      case ShapeDraw(segs, lineWidth, lineColour)  => shapeDraw(segs, lineWidth, lineColour)
      case ShapeFillDraw(segs, fillColour, lineWidth, lineColour) => shapeFillDraw(segs, fillColour, lineWidth, lineColour) 
      case ArcDraw(arc, lineWidth, lineColour) => arcDraw(arc, lineWidth, lineColour)
      case bd: BezierDraw => bezierDraw(bd)
      case TextGraphic(posn, text, fontSize, colour, align) => textGraphic(posn, text, fontSize, colour, align)
   }    
}