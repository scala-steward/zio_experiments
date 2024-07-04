import sbt._

object Dependencies {
  object Versions {
    val circe = "0.14.9"
    val ciris = "1.2.1"
    val http4s = "0.21.20"
    val http4sTracer = "1.5.3"
    val jaeger = "1.5.0"
    val logback = "1.2.3"
    val opentracing = "0.33.0"
    val zio = "1.0.4-2"
    val zioOpenTracing = "0.7.2"
    val zioInteropCats = "2.3.1.0"
    val zioLogging = "0.5.7"
    val zipkin = "2.16.3"

    val scalaTest = "3.1.1"

    val betterMonadicFor = "0.3.1"
    val kindProjector = "0.11.1"
  }
  lazy val ciris = Seq(
    "is.cir" %% "ciris" % Versions.ciris,
    "is.cir" %% "ciris-enumeratum" % Versions.ciris
  )
  lazy val openTracing = Seq(
    "io.opentracing" % "opentracing-api" % Versions.opentracing,
    "io.opentracing" % "opentracing-mock" % Versions.opentracing,
    "dev.zio" %% "zio-opentracing" % Versions.zioOpenTracing
  )

  lazy val jaegerTracer = Seq(
    "io.jaegertracing" % "jaeger-core" % Versions.jaeger,
    "io.jaegertracing" % "jaeger-client" % Versions.jaeger,
    "io.jaegertracing" % "jaeger-zipkin" % Versions.jaeger
  )

  lazy val opentracingExample = Seq(
    "io.zipkin.reporter2" % "zipkin-reporter" % Versions.zipkin,
    "io.zipkin.reporter2" % "zipkin-sender-okhttp3" % Versions.zipkin
  )

  lazy val zio = Seq(
    "dev.zio" %% "zio" % Versions.zio,
    "dev.zio" %% "zio-interop-cats" % Versions.zioInteropCats,
    "dev.zio" %% "zio-logging" % Versions.zioLogging,
    "dev.zio" %% "zio-logging-slf4j" % Versions.zioLogging,
    "dev.zio" %% "zio-test" % Versions.zio % Test,
    "dev.zio" %% "zio-test-sbt" % Versions.zio % Test
  )

  lazy val zipkin = Seq(
    "io.zipkin.reporter2" % "zipkin-reporter" % Versions.zipkin,
    "io.zipkin.reporter2" % "zipkin-sender-okhttp3" % Versions.zipkin
  )

  lazy val http4s = Seq(
    "org.http4s" %% "http4s-core" % Versions.http4s,
    "org.http4s" %% "http4s-blaze-server" % Versions.http4s,
    "org.http4s" %% "http4s-dsl" % Versions.http4s,
    "org.http4s" %% "http4s-circe" % Versions.http4s,
    "dev.profunktor" %% "http4s-tracer" % Versions.http4sTracer
  )

  lazy val circe = Seq(
    "io.circe" %% "circe-generic" % Versions.circe
  )

  lazy val logback = "ch.qos.logback" % "logback-classic" % Versions.logback

  // Tests
  lazy val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest % Test

  // Compiler
  lazy val betterMonadicFor = "com.olegpy" %% "better-monadic-for" % Versions.betterMonadicFor
  lazy val kindProjector = "org.typelevel" %% "kind-projector" % Versions.kindProjector
}
