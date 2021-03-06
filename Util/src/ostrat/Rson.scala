/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat

class Rval(val str: String) extends AnyVal
{
  def - [A](value: A)(implicit ev: Persist[A]): Rval = new Rval(str + "\n" + ev.show(value) + ";")
}

object Rval
{
  def apply[A](value: A)(implicit ev: Persist[A]): Rval = new Rval(ev.show(value) + ";")
}

class Sett(val str: String) extends AnyVal
{
  def ap[A](setting: String, value: A)(implicit ev: Persist[A]): Sett =
  {
    new Sett(str + "\n" + setting + " = " + ev.show(value) + ";")
  }
}

object Sett
{
  def apply[A](setting: String, value: A)(implicit ev: Persist[A]): Sett = new Sett(setting + " = " + ev.show(value) + ";")
}

