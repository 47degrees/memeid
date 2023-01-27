import sbt._

import sbt.Keys._
import sbt.plugins.JvmPlugin

object dependencies extends AutoPlugin {

  val scala2_12 = "2.12.17"

  val scala2_13 = "2.13.10"

  val scala3 = "3.2.1"

  private val common = List("org.specs2" %% "specs2-scalacheck" % "4.19.0" % Test)

  private val parallel =
    Def.setting {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, major)) if major <= 12 =>
          Seq()
        case _ =>
          Seq("org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4" % Test)
      }
    }

  private val cats = List(
    "org.typelevel" %% "cats-effect"       % "3.4.5",
    "org.typelevel" %% "cats-laws"         % "2.9.0"  % Test,
    "org.typelevel" %% "discipline-specs2" % "1.4.0"  % Test,
    "org.specs2"    %% "specs2-cats"       % "4.19.0" % Test
  )

  private val literal = Def.setting {
    Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided,
      "com.chuusai"   %% "shapeless"     % "2.3.10"           % Test
    )
  }

  private val doobie = List(
    "org.tpolecat" %% "doobie-core"  % "1.0.0-RC2",
    "org.tpolecat" %% "doobie-h2"    % "1.0.0-RC2" % Test,
    "org.tpolecat" %% "doobie-munit" % "1.0.0-RC2" % Test
  )

  private val circe = List(
    "io.circe"      %% "circe-core"        % "0.14.3",
    "org.typelevel" %% "discipline-specs2" % "1.4.0"  % Test,
    "io.circe"      %% "circe-testing"     % "0.14.3" % Test
  )

  private val http4s = List(
    "org.http4s" %% "http4s-core" % "0.23.18",
    "org.http4s" %% "http4s-dsl"  % "0.23.18" % Test
  )

  private val tapir = List(
    "com.softwaremill.sttp.tapir"   %% "tapir-core"         % "1.2.6",
    "com.softwaremill.sttp.tapir"   %% "tapir-openapi-docs" % "1.2.6" % Test,
    "com.softwaremill.sttp.apispec" %% "openapi-circe-yaml" % "0.3.2" % Test
  )

  private val fuuid = List(
    "io.chrisdavenport" %% "fuuid" % "0.8.0-M2"
  )

  private val scalacheck = List(
    "org.scalacheck" %% "scalacheck" % "1.17.0"
  )

  private val documentation = List(
    "org.typelevel"               %% "cats-effect" % "3.4.5",
    "io.circe"                    %% "circe-core"  % "0.14.3",
    "org.tpolecat"                %% "doobie-h2"   % "1.0.0-RC2",
    "org.http4s"                  %% "http4s-dsl"  % "0.23.18",
    "org.scalacheck"              %% "scalacheck"  % "1.17.0",
    "com.softwaremill.sttp.tapir" %% "tapir-core"  % "1.2.6",
    "io.chrisdavenport"           %% "fuuid"       % "0.8.0-M2"
  )

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = JvmPlugin

  override def projectSettings: List[Def.Setting[_]] =
    List(
      testFrameworks       += new TestFramework("munit.Framework"),
      libraryDependencies ++= common ++ parallel.value,
      libraryDependencies ++= {
        projectID.value.name match {
          case "documentation"       => documentation
          case "microsite"           => documentation
          case "memeid4s-cats"       => cats
          case "memeid4s-literal"    => literal.value
          case "memeid4s-doobie"     => doobie
          case "memeid4s-circe"      => circe
          case "memeid4s-http4s"     => http4s
          case "memeid4s-tapir"      => tapir
          case "memeid4s-fuuid"      => fuuid
          case "memeid4s-scalacheck" => scalacheck
          case _                     => Nil
        }
      }
    )

}
