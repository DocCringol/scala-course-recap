import Dependencies.*

val scala2Version = "2.13.15"
val scala3Version = "3.5.2"

ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val serialization = project
  .in(file("homeworks/serialization"))
  .settings(
    name := "homeworks-serialization",
    scalaVersion := scala2Version,

    libraryDependencies ++= List(
      catsEffect,
      shapeless,
      scalaTest  % Test,
      catsTest   % Test,
      scalaCheck % Test
    )
  )