import sbt.Keys.*
import sbt.*

import scala.language.postfixOps

val appVersion = "0.1"
val pluginVersion = "1.0.0"
val scalaAppVersion = "3.3.1"

val akkaVersion = "2.8.5"
val log4jVersion = "1.2.17"
val scalaTestVersion = "3.3.0-SNAP4"
val slf4jVersion = "2.0.5"
val snappyJavaVersion = "1.1.9.1"

lazy val testDependencies = Seq(
  libraryDependencies ++= Seq(
    "log4j" % "log4j" % log4jVersion,
    "org.slf4j" % "slf4j-api" % slf4jVersion,
    "org.slf4j" % "slf4j-log4j12" % slf4jVersion,
    "org.scalatest" %% "scalatest" % scalaTestVersion % Test
  ))

/////////////////////////////////////////////////////////////////////////////////
//      Root Project - builds all artifacts
/////////////////////////////////////////////////////////////////////////////////

/**
 * @example sbt "project root" assembly
 */
lazy val root = (project in file("./app")).
  settings(testDependencies *).
  settings(
    name := "core",
    organization := "com.github.ldaniels528.v88",
    description := "Virtual Intel 8086/8088 CPU emulator",
    version := appVersion,
    scalaVersion := scalaAppVersion,
    Compile / console / scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-language:implicitConversions", "-Xlint"),
    Compile / doc / scalacOptions += "-no-link-warnings",
    autoCompilerPlugins := true,
    assembly / mainClass := Some("com.lollypop.repl.LollypopREPL"),
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", _*) => MergeStrategy.discard
      case PathList("org", "apache", _*) => MergeStrategy.first
      case PathList("akka-http-version.conf") => MergeStrategy.concat
      case PathList("reference.conf") => MergeStrategy.concat
      case PathList("version.conf") => MergeStrategy.concat
      case _ => MergeStrategy.first
    },
    assembly / test := {},
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion
    ))


// loads the Scalajs-io root project at sbt startup
onLoad in Global := (Command.process("project root", _: State)) compose (onLoad in Global).value
