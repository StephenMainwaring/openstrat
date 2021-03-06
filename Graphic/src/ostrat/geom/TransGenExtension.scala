package ostrat
package geom

/** This is the base trait for TransExtension[T] and TransDistExtension. An object that can transform itself in a 2d geometry. This is a key trait, the object can be
 *  transformed in 2 dimensional space. Leaf classes must implement the single method fTrans(f: VT => VT):  T. */
trait TransGenExtension[T] extends Any
{ 
  def rotate(angle: Angle): T 
  def rotateRadians(r: Double): T 

  import math.Pi
  /** Rotates 30 degrees anti-clockwise or + Pi/3 */
  def anti30: T = rotate(Angle(Pi / 6))
  /** Rotates 45 degrees anti-clockwise or + Pi/4 */
  def anti45: T = rotate(Angle(Pi / 4))
  /** Rotates 60 degrees anti-clockwise or + Pi/3 */
  def anti60: T  = rotate(Angle(Pi / 3))  
  /** Rotates 90 degrees anti-clockwise or + Pi/2 */
  def anti90: T = rotate(Angle(Pi / 2))  
  /** Rotates 120 degrees anti-clockwise or + 2 * Pi/3 */
  def anti120: T = rotate(Angle(2 * Pi / 3))
  /** Rotates 135 degrees anti-clockwise or + 3 * Pi/4 */
  def anti135: T = rotate(Angle(3 * Pi / 4))
  /** Rotates 150 degrees anti-clockwise or + 5 * Pi/6 */
  def anti150: T = rotate(Angle(5 * Pi / 6))
   
  /** Rotates 30 degrees clockwise or - Pi/3 */
  def clk30: T = rotate(Angle(-Pi / 6))
  /** Rotates 45 degrees clockwise or - Pi/4 */
  def clk45: T = rotate(Angle(-Pi / 4))
  /** Rotates 60 degrees clockwise or - Pi/3 */
  def clk60: T  = rotate(Angle(-Pi / 3))  
  /** Rotates 90 degrees clockwise or - Pi / 2 */
  def clk90: T = rotate(Angle(-Pi / 2))  
  /** Rotates 120 degrees clockwise or - 2 * Pi/3 */
  def clk120: T = rotate(Angle(-2 * Pi / 3))
  /** Rotates 135 degrees clockwise or - 3 * Pi/ 4 */
  def clk135: T = rotate(Angle(-3 * Pi / 4))
  /** Rotates 150 degrees clockwise or - 5 * Pi/ 6 */
  def clk150: T = rotate(Angle(-5 * Pi / 6))
   
  /** Produces a regular cross of a sequence of four of the elements rotated */
  def rCross: Seq[T] = (1 to 4).map(i => rotate(deg90 * i))
}