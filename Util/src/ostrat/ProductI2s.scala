/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat

trait ProductI2s[A <: ProdI2] extends Any with ProductInts[A]
{ 
  override def productSize: Int = 2  
  def newElem(i1: Int, i2: Int): A
  final override def apply(index: Int): A = newElem(arr(2 * index), arr(2 * index + 1))

  final def setElem(index: Int, elem: A): Unit = { arr(2 * index) = elem._1; arr(2 * index + 1) = elem._2 }

  def head1: Int = arr(0)
  def head2: Int = arr(1)
  
  def mapBy2[B](f: (Int, Int) => B)(implicit m: scala.reflect.ClassTag[B]): Array[B] =
  {
    val newArr = new Array[B](length)
    var count = 0
    while (count < length) 
    { newArr(count) = f(arr(count * 2), arr(count * 2 + 1))
      count += 1
    }
    newArr
   }  
}

abstract class ProductI2sCompanion[A <: ProdI2, M <: ProductI2s[A]]
{ val factory: Int => M

  def apply(elems: A*): M =
  { val arrLen: Int = elems.length * 2
    val res = factory(elems.length)
    var count: Int = 0
    while (count < arrLen)
    {
      res.arr(count) = elems(count / 2)._1
      count += 1
      res.arr(count) = elems(count / 2)._2
      count += 1
    }
    res
  }
   
  def ints(elems: Int*): M =
  { val arrLen: Int = elems.length
    val res = factory(elems.length / 2)
    var count: Int = 0
    while (count < arrLen) { res.arr(count) = elems(count); count += 1 }
    res
  }
  
  def fromSeq(list: Seq[A]): M = 
   {
      val arrLen: Int = list.length * 2
      val res = factory(list.length)
      var count: Int = 0
      var rem = list
      while (count < arrLen)
      {
         res.arr(count) = rem.head._1
         count += 1
         res.arr(count) = rem.head._2
         count += 1
         rem = rem.tail
      }
      res
   }  
}