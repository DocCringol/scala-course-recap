import sbt.*

object Dependencies {

  val scalaTest  = "org.scalatest"     %% "scalatest"                     % "3.2.19"
  val catsTest   = "org.typelevel"     %% "cats-effect-testing-scalatest" % "1.5.0"
  val catsEffect = "org.typelevel"     %% "cats-effect"                   % "3.5.4"
  val scalaCheck = "org.scalatestplus" %% "scalacheck-1-18"               % "3.2.19.0"
  val shapeless  = "com.chuusai"       %% "shapeless"                     % "2.3.12"

  object sttp {

    val version = "4.0.0-M19"

    val core = "com.softwaremill.sttp.client4" %% "core" % version
    val cats = "com.softwaremill.sttp.client4" %% "cats" % version

  }

  object pureconfig {

    val version = "0.17.8"

    val core     = "com.github.pureconfig" %% "pureconfig-core"           % version
    val generic  = "com.github.pureconfig" %% "pureconfig-generic"        % version
    val generic3 = "com.github.pureconfig" %% "pureconfig-generic-scala3" % version
    val cats     = "com.github.pureconfig" %% "pureconfig-cats-effect"    % version

  }
}