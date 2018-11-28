
name := "untitled"

version := "0.1"

scalaVersion := "2.12.7"

val akkaHttpVersion = "10.1.0"
val akkaHttpJsonVersion = "1.20.0"
val monixVersion = "3.0.0-RC1"
val scalaOpenRtbVersion = "1.1.3"

resolvers ++= Seq(
  "Powerspace Artifactory" at "https://build.powerspace.com/artifactory/libs-release/",
  "Powerspace Snapshot" at "https://build.powerspace.com/artifactory/libs-snapshot/"
) :+ Resolver.jcenterRepo

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
  "de.heikoseeberger" %% "akka-http-play-json" % akkaHttpJsonVersion,
  "io.monix" %% "monix" % monixVersion,
  "com.powerspace.openrtb" %% "openrtb-json" % scalaOpenRtbVersion,
  "com.powerspace.openrtb" %% "bidswitch-json" % scalaOpenRtbVersion,
  "org.typelevel" %% "cats-effect" % "0.10.1",
  "org.typelevel" %% "cats-core" % "1.1.0",
  "org.typelevel" %% "cats-laws" % "1.1.0"
)