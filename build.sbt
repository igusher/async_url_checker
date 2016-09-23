import Dependencies._
import UrlCheckerSettings._
import sbt.Keys._


lazy val url_checker_api = (project in file("url-checker-api"))
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(PlayScala)
  .enablePlugins(JettyPlugin)
  .settings(apiSettings: _*)
  .settings(
    packageDescription := "Url checker apis"
  )

lazy val root = (project in file(".")).aggregate(url_checker_api)
  .settings(rootSettings: _*)
