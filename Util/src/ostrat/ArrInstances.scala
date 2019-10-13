package ostrat
import collection.mutable.ArrayBuffer, annotation.unchecked.uncheckedVariance, reflect.ClassTag

class Refs[+A <: AnyRef](val array: Array[A] @uncheckedVariance) extends AnyVal with ImutArr[A]
{ override def length: Int = array.length
  override def apply(index: Int): A = array(index)
  def ++ [AA >: A <: AnyRef](op: Refs[AA] @uncheckedVariance)(implicit ct: ClassTag[AA]): Refs[AA] =
  { val newArray = new Array[AA](length + op.length)
    array.copyToArray(newArray)
    op.array.copyToArray(newArray, length)
    new Refs(newArray)
  }
}
object Refs
{ def apply[A <: AnyRef](input: A*)(implicit ct: ClassTag[A]): Refs[A] = new Refs(input.toArray)
}

class BuffRefs(val buffer: ArrayBuffer[Int]) extends AnyVal with BuffArr[Int]
{ override def length: Int = buffer.length
  override def apply(index: Int): Int = buffer(index)
}

class MutRefs(val array: Array[Int]) extends AnyVal with MutArr[Int]
{ override def length: Int = array.length
  override def apply(index: Int): Int = array(index)
}

class Ints(val array: Array[Int]) extends AnyVal with ImutArr[Int]
{ override def length: Int = array.length
  override def apply(index: Int): Int = array(index)
  def ++ (op: Ints): Ints =
  { val newArray = new Array[Int](length + op.length)
    array.copyToArray(newArray)
    op.array.copyToArray(newArray, length)
    new Ints(newArray)
  }
}
object Ints
{ def apply(input: Int*): Ints = new Ints(input.toArray)
}

class BuffInts(val buffer: ArrayBuffer[Int]) extends AnyVal with BuffArr[Int]
{ override def length: Int = buffer.length
  override def apply(index: Int): Int = buffer(index)
}

class MutInts(val array: Array[Int]) extends AnyVal with MutArr[Int]
{ override def length: Int = array.length
  override def apply(index: Int): Int = array(index)
}

class DFloats(val array: Array[Double]) extends AnyVal with ImutArr[Double]
{ override def length: Int = array.length
  override def apply(index: Int): Double = array(index)
  def ++ (op: DFloats): DFloats =
  { val newArray = new Array[Double](length + op.length)
    array.copyToArray(newArray)
    op.array.copyToArray(newArray, length)
    new DFloats(newArray)
  }
}

object DFloats
{ def apply(input: Double*): DFloats = new DFloats(input.toArray)
}

class BuffDou(val buffer: ArrayBuffer[Double]) extends AnyVal with BuffArr[Double]
{ override def length: Int = buffer.length
  override def apply(index: Int): Double = buffer(index)
}

class MutDou(val array: Array[Double]) extends AnyVal with MutArr[Double]
{ override def length: Int = array.length
  override def apply(index: Int): Double = array(index)
}