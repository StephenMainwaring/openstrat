/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat

/** A variation on the Show trait for traits, classes and objects that you control. Named after the popular "The Wire" character. You will still need to
  * create an implicit Persist object in the companion object of your type, but this trait does allow you to automatically delegate toString to the
  * String / Persist implementation. */
trait Stringer extends Any
{ def typeSym: Symbol
  def typeStr: String = typeSym.name
  def str: String
  final override def toString = str
  //def persistD2(d1: Double, d2: Double): String = typeStr + (d1.toString + "; " + d2.toString).enParenth
  def persist1[T1](v1: T1)(implicit ev1: Persist[T1]): String = typeStr + ev1.persist(v1).enParenth
  def persist2[T1, T2](v1: T1, v2: T2)(implicit ev1: Persist[T1], ev2: Persist[T2]): String =
    typeStr + ev1.persist(v1).semicolonAppend(ev2.persist(v2)).enParenth
  
  def persist3[T1, T2, T3](v1: T1, v2: T2, v3: T3)(implicit ev1: Persist[T1], ev2: Persist[T2], ev3: Persist[T3]): String =
    typeStr + ev1.persist(v1).semicolonAppend(ev2.persist(v2), ev3.persist(v3)).enParenth
  
  def persist4[T1, T2, T3, T4](v1: T1, v2: T2, v3: T3, v4: T4)(implicit ev1: Persist[T1], ev2: Persist[T2], ev3: Persist[T3], ev4: Persist[T4]):
  String = typeStr + ev1.persist(v1).semicolonAppend(ev2.persist(v2), ev3.persist(v3), ev4.persist(v4)).enParenth
  
  def persist5[T1, T2, T3, T4, T5](v1: T1, v2: T2, v3: T3, v4: T4, v5: T5)(implicit ev1: Persist[T1], ev2: Persist[T2], ev3: Persist[T3],
      ev4: Persist[T4], ev5: Persist[T5]): String =
    typeStr + ev1.persist(v1).semicolonAppend(ev2.persist(v2), ev3.persist(v3), ev4.persist(v4), ev5.persist(v5)).enParenth
  def persist6[T1, T2, T3, T4, T5, T6](v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6)(implicit ev1: Persist[T1], ev2: Persist[T2], ev3: Persist[T3],
      ev4: Persist[T4], ev5: Persist[T5], ev6: Persist[T6]): String =
    typeStr + ev1.persist(v1).semicolonAppend(ev2.persist(v2), ev3.persist(v3), ev4.persist(v4), ev5.persist(v5), ev6.persist(v6)).enParenth  
    
}

/** Product2[Double, Double] with Stringer. These are used in DoubleProduct2s Array[Double] based collections. */
trait ProdD2 extends Any with Product2[Double, Double] with Stringer { final override def str = typeStr + (_1.toString + "; " + _2.toString).enParenth }
/** Product2[Int, Int] with Stringer. These are used in IntProduct2s Array[Double] based collections. */
trait ProdI2 extends Any with Product2[Int, Int] with Stringer { final override def str =
  typeStr + (_1.toString + "; " + _2.toString).enParenth}
/** Product3[Double, Double, Double]. These are used in DoubleProduct3s Array[Double] based collections. */
trait ProdD3 extends Any with Product3[Double, Double, Double] with Stringer { final override def str =
  typeStr + (_1.toString + "; " + _2.toString).enParenth}
/** Product4[Double, Double, Double, Double]. These are used in DoubleProduct4s Array[Double] based collections. */
trait ProdD4 extends Any with Product4[Double, Double, Double, Double] with Stringer { final override def str =
  typeStr + (_1.toString + "; " + _2.toString).enParenth}
/** Product5[Double, Double, Double, Double, Double]. These are used in DoubleProduct5s Array[Double] based collections. */
trait ProdD5 extends Any with Product5[Double, Double, Double, Double, Double] with Stringer { final override def str = ??? }
/** Product6[Double, Double, Double, Double, Double, Double]. These are used in DoubleProduct6s Array[Double] based collections. */
trait ProdD6 extends Any with Product6[Double, Double, Double, Double, Double, Double] with Stringer { final override def str = ??? }