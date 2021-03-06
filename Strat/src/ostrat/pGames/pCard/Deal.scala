/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pGames
package pCard

abstract class Deal(dollarsIn: Int, bet: Int, playerIn: Cards, houseIn: Cards) extends BJack
{
   val dollars = dollarsIn
   val house: Cards = houseIn
   val player: Cards = playerIn   
}