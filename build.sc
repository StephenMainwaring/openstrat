// build.sc
import mill._, scalalib._, scalajslib._, scalanativelib._, publish._

trait Common extends ScalaModule
{ def version = "0.0.5snap"  
  def scalacOptions = Seq("-feature", "-language:higherKinds,implicitConversions", "-deprecation", "-Ywarn-value-discard", "-target:jvm-1.8", "-encoding", "UTF-8", "-unchecked", "-Xlint")
}

trait CommonStd extends Common
{ def scalaVersion = "2.13.1"
}

trait CommonStdJs extends ScalaJSModule with CommonStd
{ def scalaJSVersion = "0.6.29" 
}

trait PlatformsModule extends ScalaModule with CommonStd
{ outer =>  
  
  def sources = T.sources(millSourcePath / 'src, millSourcePath / 'jvm / 'src)

  trait InnerJs extends CommonStdJs
  { def sources = T.sources(outer.millSourcePath / 'src, millSourcePath / 'src)	  
	def ivyDeps = outer.ivyDeps() ++  Agg(ivy"org.scala-js::scalajs-dom_sjs0.6:0.9.7")
  }

  trait InnerNative extends ScalaNativeModule with CommonStd
  { def scalaVersion = "2.11.12"
    def scalaNativeVersion = "0.3.8"  
	def sources = T.sources(outer.millSourcePath / 'src, outer.millSourcePath / 'srcNat)
	def ivyDeps = outer.ivyDeps() //++ ivyNat()	 
  }

  trait InnerTests extends Tests
  { def ivyDeps = Agg(ivy"com.lihaoyi::utest:0.6.9")
    def testFrameworks = Seq("utest.runner.Framework") 
    def sources = T.sources(millSourcePath / 'src)  
  }

  trait InnerExamples extends ScalaModule with CommonStd
  {
  	def sources = T.sources(outer.millSourcePath / 'src, millSourcePath / 'src)
  }
}

object Util extends PlatformsModule
{
  object MacrosJvm extends CommonStd with PublishModule
  { def ivyDeps = Agg(ivy"${scalaOrganization()}:scala-reflect:${scalaVersion()}")
    def sources = T.sources(Util.millSourcePath / 'Macros / 'src)
    def publishVersion = "0.0.5"
    def pomSettings = PomSettings(
      description = "openstrat",
      organization = "com.richstrat",
      url = "https://github.com/richtype/openstrat",
      licenses = Seq(License.MIT),
      versionControl = VersionControl.github("richtype", "openstrat"),
      developers = Seq(
        Developer("richtype", "Rich Oliver","https://github.com/richtype")
      )
    )
  }

  object MacrosJs extends CommonStdJs

  def moduleDeps = Seq(MacrosJvm)
  object test extends InnerTests
  
  object js extends InnerJs
  { def moduleDeps = Seq(MacrosJs)
  }

  object examples extends InnerExamples
  {	def moduleDeps = Seq(Util)
  }

  object Nat extends InnerNative  
}

object Graphic extends PlatformsModule
{ def moduleDeps = Seq(Util)  
  object test extends InnerTests
  
  object js extends InnerJs {  def moduleDeps = Seq(Util.js)  }
  object Nat extends InnerNative

  object examples extends InnerExamples
  { def moduleDeps = Seq(Graphic)
  }
}

object World extends PlatformsModule
{ def moduleDeps = Seq(Graphic)  

  object test extends InnerTests
      
  object js extends InnerJs
  { def moduleDeps = Seq(Graphic.js) 
  }
  
  object Nat extends InnerNative
}

object Strat extends PlatformsModule
{ def moduleDeps = Seq(World)
  

  object test extends InnerTests
      
  object js extends InnerJs { def moduleDeps = Seq(World.js) }
  object Nat extends InnerNative
}

object Dev extends PlatformsModule
{ def moduleDeps = Seq(Strat)
  def mainClass = Some("ostrat.pFx.DevApp")
  def sources = T.sources(millSourcePath / 'src, millSourcePath / 'jvm / 'src, Graphic.millSourcePath / 'examples / 'src)
  def resources = T.sources(millSourcePath / 'mine)

  object js extends InnerJs
  { def moduleDeps = Seq(Strat.js)
    def sources = T.sources(millSourcePath / 'src, Dev.millSourcePath / 'srcLearn)
  } 
}

def run() = Dev.runBackground()
def test = Util.test
def jsfast = Dev.js.fastOpt
def jsfull = Dev.js.fullOpt