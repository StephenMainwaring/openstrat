package ostrat
package pParse

/** Not entirely sure what this does. */
object PrefixPlus
{
  def apply(implicit refs: Refs[TokenOrBlock]): EMonArr[TokenOrBlock] =
  {
    val acc: Buff[TokenOrBlock] = Buff()

    def loop(rem: RefsOff[TokenOrBlock]): EMonArr[TokenOrBlock] = rem match
    { case RefsOff0() => Good(acc).map(_.toArr)
      case RefsOff2Tail(pp: PrefixToken,  right: Expr, tail) => { acc.append(PreOpExpr(pp, right)); loop(tail) }
      case RefsOffHead(pp: PrefixToken) => bad1(pp, "Prefix operator not followed by expression")
      case RefsOff1Tail(h, tail) => { acc.append(h); loop(tail) }
    }
    loop(refs.offset0)
  }
}

/** Not sure what this does. */
object getBlocks
{
  def apply(seg: Arr[ExprMember]): EMon[Expr]= sortBlocks(seg.toList, Buff()).flatMap {
    case Arr(e: Expr) => Good(e)
    case s => bad1(s.head, "Unknown Expression sequence in getBlocks:" -- s.toString)
  }

  def sortBlocks(rem: List[ExprMember], acc: Buff[TokenOrBlock]): EMonArr[TokenOrBlock] = rem match
  { case Nil => PrefixPlus(acc.toRefs)
    case (at: IdentifierLowerToken) :: (bb: BracketedStatements) :: t2 =>
    { //typedSpan needs removal
      val (blocks, tail) = t2.typedSpan[BracketedStatements](_.isInstanceOf[BracketedStatements])
      sortBlocks(tail, acc :+ AlphaBracketExpr(at, blocks.toImut.asInstanceOf[Refs[BracketedStatements]]))
    }
    case h :: tail => sortBlocks(tail, acc :+ h)
  }
}

/** Needs Testing. */
object getExpr
{
  def apply (implicit seg: Refs[ExprMember]): EMon[Expr] =
  {
    val acc: Buff[ExprMember] = Buff()

    def loop(rem: RefsOff[ExprMember]): EMon[Expr] = rem match
    { case RefsOff0() => getBlocks(acc.toArr)

      case RefsOff1Tail(at: AsignToken, tail) => for {
        gLs <- getBlocks(acc.toArr);
        gRs <- loop(tail) //This has been altered. I think its correct now with no altering to acc
      } yield AsignExpr(at, gLs, gRs)

      case RefsOff1Tail(h, tail) => { acc.append(h) ;loop(tail) }
    }

    loop(seg.offset0)
  }
}