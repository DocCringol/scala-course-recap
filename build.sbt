import Dependencies.*

val scala2Version = "2.13.15"
val scala3Version = "3.5.2"

ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val serializationHW = project
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

lazy val decoratorsP = project
  .in(file("practices/decorators"))
  .settings(
    name := "practices-decorators",
    scalaVersion := scala3Version,

    libraryDependencies ++= List(
      catsEffect,
      sttp.core,
      sttp.cats,
      pureconfig.core,
      pureconfig.cats,
      pureconfig.generic3
    )
  )