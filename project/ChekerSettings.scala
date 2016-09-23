import play.sbt.routes.RoutesKeys._
import sbt._
import sbt.Keys._
import Dependencies._

object UrlCheckerSettings {


  lazy val commonSettings = Seq(
    scalaVersion := "2.11.7",
    publishMavenStyle := true,
    libraryDependencies ++= serviceDeps,
    parallelExecution in ThisBuild := false
  )

  lazy val rootSettings = commonSettings ++ Seq(name := "analysis",
    fork := true,
    parallelExecution in Global := false
  )

  lazy val apiSettings = commonSettings ++ Seq(name := "url-checker-api",
    libraryDependencies ++= serviceDeps,
    routesGenerator := InjectedRoutesGenerator,
    fork := true)
}
