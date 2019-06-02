/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat

/** The Multiple type class allow you to represent multiple values of type A. Implicit conversion in package object. */
case class Multiple[+A](value: A, num: Int)
{ def * (operand: Int): Multiple[A] = Multiple(value, num * operand)
  def toSeq: Seq[A] = (0 until num).map(_ => value)
  def toList: List[A] = toSeq.toList
  def map[B](f: A => B): Multiple[B] = Multiple[B](f(value), num)
  def flatMap[B](f: A => Multiple[B]) =
  { val res = f(value)
     Multiple[B](res.value, res.num * num)
  }
  override def toString = "Multiple" + (value.toString + "; " + num.toString).enParenth
}

/** Companion object for the Multiple[+A] type class. */
object Multiple
{
  implicit def toMultipleImplicit[A](value: A): Multiple[A] = Multiple(value, 1)
  implicit class MultipleSeqImplicit[A](thisSeq: Seq[Multiple[A]])
  {
    def toSingles: List[A] = thisSeq.toList.flatMap(_.toList)
    def iForeachSingle(f: (A, Int) => Unit) = toSingles.iForeach(f)
  }
}