ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.9.0"

lazy val root = (project in file("."))
  .settings(
    name := "Campus-Clash",

    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.19" % Test,
      "com.lihaoyi" %% "upickle" % "4.4.3"
    ),

    coverageExcludedPackages := "campusclash\\.Main"
  )