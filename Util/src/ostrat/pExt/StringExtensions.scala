/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pExt

/** Extension methods for String. Brought into scope by the stringToImplicit method in the package object. */
class StringImplicit(val thisString: String) extends AnyVal //extends PersistStr
{  
  def parseToStatements: ERefs[pParse.Statement] = pParse.stringToStatements(thisString)
  def findTokens: ERefs[pParse.Token] = pParse.TokensFind(thisString).fromString
  def findStatements: ERefs[pParse.Statement] = findTokens.flatMap(pParse.GetStatements(_))
  //def asType[A](implicit ev: Persist[A]): EMon[A] = thisString.parseToStatements.flatMap(ev.fromStatements)
  def findType[A: Persist]: EMon[A] = thisString.parseToStatements.flatMap(_.findType[A])
  def findTypeElse[A: Persist](elseValue: => A): A = findType[A].getElse(elseValue)
  def findInt: EMon[Int] = thisString.parseToStatements.flatMap(_.findInt)
  def findDouble: EMon[Double] = thisString.parseToStatements.flatMap(_.findDouble)
  def findBoolean: EMon[Boolean] = thisString.parseToStatements.flatMap(_.findBoolean)
  def findTypeIndex[A: Persist](index: Int): EMon[A] = thisString.parseToStatements.flatMap(_.findTypeIndex[A](index))  
  def findTypeDo[A: Persist](f: A => Unit): Unit = findType[A].foreach(f)

  def asType[A](implicit ev: Persist[A]): EMon[A] = parseToStatements.flatMap(sts => sts match
    { case Refs1(h) => ev.fromStatement(h).elseTry(ev.fromStatements(sts))
      case sts => ev.fromStatements(sts)
    })
  def asInt: EMon[Int] = asType[Int]
  
  def findIntArray: EMon[Array[Int]] = thisString.parseToStatements.flatMap(_.findIntArray)
  
  def findSett[A: Persist](settingStr: String): EMon[A] = thisString.parseToStatements.flatMap(_.findSett[A](settingStr))
  def findSettElse[A: Persist](settingStr: String, elseValue: A): A = findSett[A](settingStr).getElse(elseValue)
  def findIntSett(settingStr: String): EMon[Int] = thisString.parseToStatements.flatMap(_.findIntSett(settingStr))
  def findIntSettElse(settingStr: String, elseValue: Int): Int = findIntSett(settingStr).getElse(elseValue)  
  def findDoubleSett(settingStr: String): EMon[Double] = thisString.parseToStatements.flatMap(_.findDoubleSett(settingStr))
  def findDoubleSettElse(settingStr: String, elseValue: Double): Double = findDoubleSett(settingStr).getElse(elseValue)
  def findBooleanSett(settingStr: String): EMon[Boolean] = thisString.parseToStatements.flatMap(_.findBooleanSett(settingStr))
  def findBooleanSettElse(settingStr: String, elseValue: Boolean): Boolean = findBooleanSett(settingStr).getElse(elseValue)
  
  def - (other: String): String = thisString + other
  /** Concatenates a space and then the other String */
  def -- (other: String): String = thisString + " " + other
  /** appends a newline special character to this String */
  def nl: String = thisString + "\n"
  /** Concatenates a newline special character followed by spaces to this string */
  def nl(indent: Int): String = thisString + "\n" + indent.spaces
  /** prepends a newline special character and spaces to this string */
  def preNl(indent: Int): String = thisString + "\n" + indent.spaces
  /** Prepends a newline special character to this String */
  def preNl: String = "\n" + thisString
  /** Prepends 2 spaces to string */   
  def ind2: String = "  " + thisString
  /** Prepends 4 spaces to string */
  def ind4: String = "    " + thisString
  /** Concatenates a '/' character and then the other String. Useful for constructing directory/ folder paths on the Web, Linux and Unix */      
  def / (other: String): String = thisString + "/" + other
  def :- (other: String): String = thisString + ": " + other 
  def optAppend (optionOther: Option[String]): String = optionOther.fold(thisString)(string2 => thisString + " " + string2)
  def enquote: String = "\"" + thisString + "\""
  def enquote1: String = "'" + thisString + "'"
  def addEnqu(s2: String): String = thisString + s2.enquote
  /** encloses string in parentheses */
  def enParenth: String = "(" + thisString + ")"
  /** encloses string in Square brackets */
  def enSquare: String = "[" + thisString + "]"
  /** encloses string in Curly brackets */
  def enCurly: String = "{" + thisString + "}"      
  def words: Array[String] = thisString.split("\\s+")
  def toLowerWords: Array[String] = thisString.toLowerCase.words
  def remove2ndDot: String =
  { val (s1, s2) = thisString.span(_ != '.')         
    val (s2a, s2b) = s2.drop(1).span(_ != '.')
    s1 + "." + s2a
  }
  
  def toTokens: EMon[Refs[pParse.Token]] = pParse.stringToTokens(thisString)
  /** Appends strings with a comma and space seperator */
  def appendCommas(extraStrings: String*): String = extraStrings.foldLeft(thisString)(_ + ", " + _)

  /** Appends extra Strings to thisString separated by " ;". */
  def appendSemicolons(extraStrings: String*): String =
  { val v1 = extraStrings.foldLeft(thisString)(_ + "; " + _)
    extraStrings.length match
    { case 0 => ife(thisString == "", v1 + ";", v1)
      case _ => ife(extraStrings.last == "", v1 + ";", v1)
    }
  }

  def commaInts(ints: Int*): String = ints.foldLeft(thisString)(_ + ", " + _.toString)

  def dotAppend(extraStrings: String*): String = extraStrings.foldLeft(thisString)(_ + "." + _)  
  def appendParenthSemis(innerStrs: String*): String = thisString + innerStrs.semiParenth

  def prependIndefiniteArticle = thisString.find(!_.isWhitespace) match
  { case Some(ch) => ch.toLower match
    { case 'a' | 'e' | 'i' | 'o' | 'u' => "an " + thisString
      case _ => "a " + thisString
    }
    case _ => "a " + thisString
  }

  def lengthFix(newLenIn: Int = 3, packChar: Char = ' '): String =
  { val newLen = newLenIn.min(1).max(9)
    (newLen - thisString.length) match {
      case l if l < 0 => thisString.take(newLen)
      case 0 => thisString
      case l if l.isEven => packChar.timesString(l / 2) + thisString + packChar.timesString(l / 2)
      case l => packChar.timesString(l / 2) + thisString + packChar.timesString(l / 2 + 1)
    }
  }
}
