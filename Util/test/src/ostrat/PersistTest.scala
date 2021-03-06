/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
import utest._

object PersistTest
{
  class TestClass(val str: String) extends PersistSingleton

  object TestClass
  {
    implicit object TestClassPersistImplicit extends PersistSingletons[TestClass]("TestClass")
    { override val singletonList = List(TestObjA, TestObjB)    
    }
  }

  object TestObjA extends TestClass("TestObjA")
  object TestObjB extends TestClass("TestObjB")
  
  case class My2(ints: Seq[Int], myStr: String)
 
  object My2
  { implicit val persist: Persist[My2] = Persist2[Seq[Int], String, My2]("My2", "ints", _.ints, "myStr", _.myStr, apply)
  }

  val tests = Tests
  {    

    val aa: TestClass = TestObjA
    val aaStr: String = "TestObjA"
    val str1: String = "I am a String"
    val str1Std: String = "\"I am a String\""
    val abSeq = Seq(TestObjA, TestObjB)
    val abArr = Arr(TestObjA, TestObjB)
    val mc = My2(List(7, 8, 9), "hi")
    
    'persistOther -
    {
      aa.str ==> aaStr
      aaStr.findType[TestClass] ==> Good(TestObjA)
      aa.strTyped ==> "TestClass(TestObjA)"
      abArr.str.findType[Seq[TestClass]] ==> Good(Seq(TestObjA, TestObjB))
      abSeq.str.findType[Seq[TestClass]] ==> Good(Seq(TestObjA, TestObjB))

      str1.str ==> str1Std
      str1.strSemi ==> str1Std
      str1.strComma ==> str1Std
      str1.strTyped ==> "Str(" + str1Std + ")"
      mc.str ==> "My2(7, 8, 9; \"hi\")"
    }    
  }
}