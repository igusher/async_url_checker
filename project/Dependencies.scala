import play.sbt.PlayImport._
import sbt._

object Dependencies {
  val playVersion = "2.4.6"
  val scalamockVersion = "3.2"
  val typesafeConfigVersion = "1.2.1"
  private val guiceVersion: String = "4.0"
  private val slf4jVersion: String = "1.7.5"

  val scalaTest = "org.scalatest" %% "scalatest" % "2.2.1" % "test"
  val guice = "com.google.inject" % "guice" % guiceVersion
  val typesafeConfig = "com.typesafe" % "config" % typesafeConfigVersion
  val play = "com.typesafe.play" %% "play" % playVersion
  val playJson = "com.typesafe.play" %% "play-json" % playVersion
  val scalamock = "org.scalamock" %% "scalamock-scalatest-support" % scalamockVersion % "test"
  val scalaTestPlus = "org.scalatestplus" %% "play" % "1.4.0-M3" % "test"

  val jsoup = "org.jsoup" % "jsoup" % "1.8.3"
  val commonsValidator = "commons-validator" % "commons-validator" % "1.5.1"

  val serviceDeps = Seq(ws, filters, jsoup, commonsValidator, scalaTest, guice, typesafeConfig, play, playJson, scalamock)
}
