package ostrat
import utest._

/** Test object for our own immutable wrapper. Types have been deliberatly left unannotated to test demonstrated type inference.*/
object ArrOtherTest extends TestSuite
{
  val tests = Tests
  {
    val ints1 = Ints(1, 2, 3, 4)
    val ints2 = Ints(5, 6, 7)
    val ints3 = ints1 ++ ints2
    val dbls1 = Dbls(1.5, 3, 4.5, 6)
    val dbls2 = ints1.map(_ * 1.5)
    val dblList1 = ints1.iterFlatMap{ a => List(a + 0.1, a + 0.2)}
    val longs1 = Longs(2, 4, 6) ++ Longs(8, 9)

    'test1 -
    { ints1(3) ==> 4
      ints3.length ==> 7
      ints3(6) ==> 7
      dbls1(2) ==> 4.5
      dbls2(0) ==> 1.5
      dbls2.length ==> 4
      dblList1(0) ==> 1.1
      dblList1.length ==> 8
      longs1(4) == 9l
    }
    val ints4 = ints1.bind(a => Ints(a + 10, a + 20, a + 30))
    val longs2 = ints1.bind(a => Longs(a + 100, a + 200, a + 300))
    val dbls3 = ints2.bind(i => Dbls(i, i * 0.5))
    'Bind -
    {
      ints4(1) ==> 21
      ints4(5) ==> 32
      ints4.length ==> 12
      dbls3(1) ==> 2.5
      longs2.length ==> 12
    }
  }
}