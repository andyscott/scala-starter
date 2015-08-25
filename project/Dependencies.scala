import sbt._
import Keys._

object Dependencies {
  val resolutionRepos = Seq(
    "spray repo"              at "http://repo.spray.io",
    "spray nightly repo"      at "http://nightlies.spray.io",
    "Typesafe releases"       at "http://repo.typesafe.com/typesafe/releases",
    "Sonatype OSS Snapshots"  at "https://oss.sonatype.org/content/repositories/snapshots",
    "theatr.us"               at "http://repo.theatr.us"
  )

  def compile   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "compile")
  def provided  (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "provided")
  def test      (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "test")
  def it        (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "it")
  def runtime   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "runtime")
  def container (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "container")

  // Akka
  //val akkaVersion   = "2.3.11"
  //val akkaActor     = "com.typesafe.akka" %% "akka-actor"   % akkaVersion
  //val akkaTestKit   = "com.typesafe.akka" %% "akka-testkit" % akkaVersion
  //val akkaSlf4j     = "com.typesafe.akka" %% "akka-slf4j"   % akkaVersion

  // Spray
  //val sprayVersion  = "1.3.3"
  //val sprayCan      = "io.spray" %% "spray-can"     % sprayVersion
  //val sprayCaching  = "io.spray" %% "spray-caching" % sprayVersion
  //val sprayClient   = "io.spray" %% "spray-client"  % sprayVersion
  //val sprayRouting  = "io.spray" %% "spray-routing-shapeless2" % sprayVersion
  //val sprayTestKit  = "io.spray" %% "spray-testkit" % sprayVersion


  // Others
  //val scalaLogging  = "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"
  //val scalazCore    = "org.scalaz"  %%  "scalaz-core" % "7.1.1"

  // Testing
  val scalaTest     = "org.scalatest" %% "scalatest" % "2.2.4"
}
