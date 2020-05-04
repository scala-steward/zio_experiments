import Dependencies._

ThisBuild / scalaVersion := "2.13.1"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.users"
ThisBuild / organizationName := "users"

addCompilerPlugin(kindProjector cross CrossVersion.full)
addCompilerPlugin(betterMonadicFor)

lazy val root = (project in file("."))
  .settings(
    name := "zio_experiments",
    skip in publish := true
  )
  .aggregate(users)

lazy val users = (project in file("users"))
  .settings(
    name := "users",
    resolvers += Resolver.sonatypeRepo("releases"),
    libraryDependencies ++= openTracing,
    libraryDependencies ++= jaegerTracer,
    libraryDependencies ++= zipkin,
    libraryDependencies ++= zio
  )

Global / onChangedBuildSource := ReloadOnSourceChanges
Global / testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
