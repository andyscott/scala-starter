import sbt._
import Keys._

import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._
import sbtunidoc.Plugin._
import sbtbuildinfo._
import sbtbuildinfo.BuildInfoKeys._
import de.heikoseeberger.sbtheader._

object Build extends Build {
  import BuildSettings._
  import Dependencies._

  lazy val root = Project("foo", file("."))
    .aggregate(
      project1,
      project2)
    .settings(basicSettings: _*)
    .settings(unidocSettings: _*)

  lazy val project1 = Project("project1", file("project1"))
    .settings(basicSettings: _*)
    .settings(libraryDependencies ++=
      // compile(someLibrary) ++
      test(scalaTest)
    )
    .enablePlugins(BuildInfoPlugin)
    .enablePlugins(AutomateHeaderPlugin)

  lazy val project2 = Project("project2", file("project2"))
    .dependsOn(project1 % "test")
    .settings(basicSettings: _*)
    .settings(
      buildInfoKeys := Seq[BuildInfoKey](
        version, scalaVersion, sbtVersion,
        gitCommitId, gitBranchName,
        buildTime),
      buildInfoPackage <<= (organization) {
        (organization) => s"${organization}.project2"
      }
    )
    //.settings(libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _))
    .settings(libraryDependencies ++=
      //compile(someLibrary) ++
      test(scalaTest)
    )
    .enablePlugins(AutomateHeaderPlugin)


  // configure prompt to show current project
  override lazy val settings = super.settings :+ {
    shellPrompt := { s => Project.extract(s).currentProject.id + "> " }
  }

}

object BuildSettings {

  lazy val basicSettings = seq(
    version               := "0.0.0",
    organization          := "baz.bar",
    scalaVersion          := "2.11.6",
    resolvers            ++= Dependencies.resolutionRepos,
    HeaderKey.headers     <<= (name, version) { (name, version) => Map(
      "scala" -> (HeaderPattern.cStyleBlockComment,
        """|/*
           | *  """ + name + " " + version + """
           | *--------------------------------------------------------------------*/
           |
           |""".stripMargin
          )
    ) },
    scalacOptions         := Seq(
      "-encoding", "utf8",
      "-feature",
      "-unchecked",
      "-deprecation",
      "-target:jvm-1.6",
      "-language:_",
      "-Xlog-reflective-calls",
      "-Ywarn-dead-code",
      "-Ywarn-infer-any",
      "-Ywarn-unused-import"
    ),
    cancelable in Global  := true,
    fork                  := true,
    connectInput in run   := true,
    outputStrategy        := Some(StdoutOutput),
    javaHome              := sys.env.get("ALT_JAVA_HOME").map(file)

  ) ++ formatSettings

  lazy val formatSettings = SbtScalariform.scalariformSettings ++ Seq(
    ScalariformKeys.preferences in Compile := formattingPreferences,
    ScalariformKeys.preferences in Test    := formattingPreferences
  )

  import scalariform.formatter.preferences._
  def formattingPreferences =
    FormattingPreferences()
      .setPreference(RewriteArrowSymbols,                         true)
      .setPreference(AlignParameters,                             true)
      .setPreference(AlignSingleLineCaseStatements,               true)
      .setPreference(DoubleIndentClassDeclaration,                true)
      .setPreference(PreserveDanglingCloseParenthesis,            true)
      .setPreference(MultilineScaladocCommentsStartOnFirstLine,   true)
      .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)

  def buildTime = BuildInfoKey.action("buildTime") {
    val today = java.util.Calendar.getInstance().getTime()
    val sdf = new java.text.SimpleDateFormat()
    sdf.format(today)
    //java.time.LocalDateTime.now // java 8 only
  }

  def gitCommitId = BuildInfoKey.constant("gitCommitId",
    scala.util.Try("git rev-parse HEAD".!!.trim).getOrElse("unknown"))

  def gitBranchName = BuildInfoKey.constant("gitBranchName",
    scala.util.Try("git rev-parse --abbrev-ref HEAD".!!.trim).getOrElse("unknown"))

}
