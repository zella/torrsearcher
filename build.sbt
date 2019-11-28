name := """torrsearcher"""
organization := "com.github.zella"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.1"

// major.minor are in sync with the elasticsearch releases
val elastic4sVersion = "6.7.3"

libraryDependencies += guice
libraryDependencies ++= Seq(
  "com.sksamuel.elastic4s" %% "elastic4s-core" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-http-streams" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-http" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-play-json" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-testkit" % elastic4sVersion % "test"
)
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies += "io.monix" %% "monix-reactive" % "3.1.0"

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test
libraryDependencies += "com.dimafeng" %% "testcontainers-scala" % "0.33.0" % "test"
libraryDependencies += "org.testcontainers" % "elasticsearch" % "1.12.3" % Test

Test / parallelExecution := false