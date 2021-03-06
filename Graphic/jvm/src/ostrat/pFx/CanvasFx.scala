/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pFx
import geom._, pCanv._, javafx._, scene._

/** An alternative version of CanvasFx to experiment with removing the ScalaFx dependency and just use JavaFx directly. */
case class CanvasFx(canvFx: canvas.Canvas, theScene: Scene) extends CanvasTopLeft// with CanvSaver
{
  val gc: canvas.GraphicsContext = canvFx.getGraphicsContext2D
  override def width = canvFx.getWidth.max(100)
  override def height = canvFx.getHeight.max(100)
  def getButton(e: input.MouseEvent): MouseButton = 
  { import input.MouseButton._
    e.getButton match
    { case PRIMARY => LeftButton
      case MIDDLE => MiddleButton
      case SECONDARY => RightButton
      case _ => NoButton
    }     
  }
  //I think getX is wanted not getSceneX or getScreenX
  canvFx.setOnMouseReleased((e: input.MouseEvent) => mouseUpTopLeft(e.getX(), e.getY, getButton(e)))   
  //canvFx.onMousePressed = (e: input.MouseEvent) => mouseDownTopLeft(e.x, e.y, getButton(e))   
  //canvFx.onMouseMoved = (e: input.MouseEvent) => mouseMovedTopLeft(e.x, e.y, getButton(e))    
  canvFx.setOnMouseDragged((e: input.MouseEvent) => mouseDraggedTopLeft(e.getX, e.getY, getButton(e))) 
  
  theScene.setOnKeyReleased{(e: input.KeyEvent) => deb("Pressed"); keyReleased() }

  canvFx.setOnScroll((e: input.ScrollEvent) => e.getDeltaY match
  { case 0 => //("scroll delta 0")
    case d if d > 0 => onScroll(true)
    case _ => onScroll(false)
  })

  import paint.Color
  def toFxColor(colour: Colour): Color = Color.rgb(colour.red, colour.green, colour.blue, colour.alpha / 255.0)
  override def tlPolyFill(fp: PolyFill): Unit =
  { gc.setFill(toFxColor(fp.colour))
    gc.fillPolygon(fp.xArray, fp.yArray, fp.poly.length)
  }

  /** Needs mod */
  override protected[this] def tlPolyDraw(dp: PolyDraw): Unit =
  { gc.setStroke(toFxColor(dp.colour))
    gc.setLineWidth(dp.lineWidth)
    gc.strokePolygon(dp.xArray, dp.yArray, dp.vertsLen)
  }
 
  override protected[this] def tlPolyFillDraw(pfd: PolyFillDraw): Unit =
  { gc.setFill(toFxColor(pfd.fillColour))
    gc.fillPolygon(pfd.xArray, pfd.yArray, pfd.vertsLen)
    gc.setStroke(toFxColor(pfd.lineColour))
    gc.setLineWidth(pfd.lineWidth)
    gc.strokePolygon(pfd.xArray, pfd.yArray, pfd.vertsLen)
  }

  override protected[this] def tlLinePathDraw(pod: LinePathDraw): Unit =
  { gc.beginPath
    gc.moveTo(pod.xStart, pod.yStart)
    pod.foreachEnd(gc.lineTo)
    gc.setStroke(toFxColor(pod.colour))
    gc.setLineWidth(pod.lineWidth)
    gc.stroke()
  }
   
  override protected[this] def tlLineDraw(ld: LineDraw): Unit =
  { gc.beginPath
    gc.moveTo(ld.xStart, ld.yStart)
    gc.lineTo(ld.xEnd, ld.yEnd)
    gc.setStroke(toFxColor(ld.colour))
    gc.setLineWidth(ld.width)
    gc.stroke()
  }
   
  override protected[this] def tlArcDraw(ad: ArcDraw): Unit =
  { gc.beginPath
    gc.moveTo(ad.xStart, ad.yStart)
    ad.fControlEndRadius(gc.arcTo)
    gc.setStroke(toFxColor(ad.colour))
    gc.stroke()
  }
  
  override protected[this] def tlDashedLineDraw(dld: DashedLineDraw): Unit =
  { gc.beginPath
    gc.moveTo(dld.xStart, dld.yStart)
    gc.lineTo(dld.xEnd, dld.yEnd)
    gc.setStroke(toFxColor(dld.colour))
    gc.setLineWidth(dld.lineWidth)
    gc.setLineDashes(dld.dashArr :_*)
    gc.stroke()
    gc.setLineDashes()
  }  
   
  def fxAlign(align: TextAlign) =
  { import text._
    align match
    { case CenAlign => TextAlignment.CENTER
      case LeftAlign => TextAlignment.LEFT
      case RightAlign => TextAlignment.RIGHT
    }
  }
   
  override protected[this] def tlBezierDraw(bd: BezierDraw): Unit =
  { gc.setStroke(toFxColor(bd.colour))
    gc.setLineWidth(bd.lineWidth)
    gc.beginPath
    gc.moveTo(bd.xStart, bd.yStart)
    gc.bezierCurveTo(bd.xC1, bd.yC1, bd.xC2, bd.yC2, bd.xEnd, bd.yEnd)
    gc.stroke()
  }
   
  override protected[this] def tlTextGraphic(tg: TextGraphic): Unit =
  { gc.setTextAlign(fxAlign(tg.align))
    gc.setTextBaseline(geometry.VPos.CENTER)
    gc.setFont(new text.Font(tg.fontSize))
    gc.setFill(toFxColor(tg.colour))
    gc.fillText(tg.str, tg.posn.x, tg.posn.y)
  }
   
  protected[this] def tlLinesDraw(lsd: LinesDraw): Unit =
  { gc.beginPath
    lsd.lines.foreach(ls => { gc.moveTo(ls.xStart, ls.yStart);  gc.lineTo(ls.xEnd, ls.yEnd)})
    gc.setLineWidth(lsd.lineWidth)
    gc.setStroke(toFxColor(lsd.colour))
    gc.stroke()
  }
   
  override protected[this] def tlTextOutline(to: TextOutline): Unit =
  { gc.setTextAlign(text.TextAlignment.CENTER)
    gc.setTextBaseline(geometry.VPos.CENTER)
    gc.setStroke(toFxColor(to.colour))
    gc.setLineWidth(1)
    gc.setFont(new text.Font(to.fontSize))
    gc.strokeText(to.str, to.posn.x, to.posn.y)
  }
   
  private[this] def segsPath(segs: Shape): Unit =
  { gc.beginPath
    var startPt = segs.last.pEnd
    gc.moveTo(startPt.x, startPt.y)
    segs.foreach{seg =>
      seg.segDo(ls => gc.lineTo(ls.xEnd, ls.yEnd),
        as => as.fControlEndRadius(startPt, gc.arcTo),
        bs => gc.bezierCurveTo(bs.xC1, bs.yC1, bs.xUses, bs.yUses, bs.xEnd, bs.yEnd)
      )
      startPt = seg.pEnd
    }
    gc.closePath
  }
   
  override protected[this] def tlShapeFill(sf: ShapeFill): Unit =
  { segsPath(sf.segs)
    gc.setFill(toFxColor(sf.colour))
    gc.fill()
  }
   
  override protected[this] def tlShapeFillDraw(sfd: ShapeFillDraw): Unit =
  { segsPath(sfd.segs)
    gc.setFill(toFxColor(sfd.fillColour))
    gc.fill()
    gc.setLineWidth(sfd.lineWidth)
    gc.setStroke(toFxColor(sfd.lineColour))
    gc.stroke()
  }

  override def tlShapeDraw(sd: ShapeDraw): Unit =
  { segsPath(sd.segs)
    gc.setLineWidth(sd.lineWidth)
    gc.setStroke(toFxColor(sd.colour))
    gc.stroke()
  }

  override def clear(colour: Colour): Unit =
  { gc.setFill(toFxColor(colour))
    gc.fillRect(0, 0, width, height)
  }

  def getTime: Long = System.currentTimeMillis
  import animation._
  override def timeOut(f: () => Unit, millis: Integer): Unit = new Timeline(new KeyFrame(util.Duration.millis(millis.doubleValue()),
    (ae: event.ActionEvent) => f())).play
   
  override protected[this] def tlClip(pts: Polygon): Unit =
  { gc.beginPath
    gc.moveTo(pts.head1, pts.head2)
    pts.foreachPairTail(gc.lineTo)
    gc.closePath()
    gc.clip()
  }

  override def gcSave(): Unit = gc.save()
  override def gcRestore(): Unit = gc.restore()
  def saveFile(fileName: String, output: String): Unit = saveRsonFile(yourDir, fileName, output: String)
  def loadFile(fileName: String): EMon[String] = loadRsonFile(yourDir / fileName)
}