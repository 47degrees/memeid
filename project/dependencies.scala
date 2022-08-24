import sbt._

import sbt.Keys._
import sbt.plugins.JvmPlugin

object dependencies extends AutoPlugin {

  private val common = List("org.specs2" %% "specs2-scalacheck" % "4.16.1" % Test)

  private val parallel = on(2, 13) {
    "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4" % Test
  }

  private val cats = List(
    "org.typelevel" %% "cats-effect"       % "3.3.14",
    "org.typelevel" %% "cats-laws"         % "2.8.0"  % Test,
    "org.typelevel" %% "discipline-specs2" % "1.4.0"  % Test,
    "org.specs2"    %% "specs2-cats"       % "4.16.1" % Test
  )

  private val literal = Def.setting {
    Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided,
      "com.chuusai"   %% "shapeless"     % "2.3.9"            % Test
    )
  }

  private val doobie = List(
    "org.tpolecat" %% "doobie-core"   % "1.0.0-RC2",
    "org.tpolecat" %% "doobie-specs2" % "1.0.0-RC2" % Test,
    "org.tpolecat" %% "doobie-h2"     % "1.0.0-RC2" % Test,
    "org.specs2"   %% "specs2-cats"   % "4.16.1"    % Test
  )

  private val circe = List(
    "io.circe"      %% "circe-core"        % "0.14.2",
    "org.typelevel" %% "discipline-specs2" % "1.4.0"  % Test,
    "io.circe"      %% "circe-testing"     % "0.14.2" % Test
  )

  private val http4s = List(
    "org.http4s" %% "http4s-core" % "0.23.13",
    "org.http4s" %% "http4s-dsl"  % "0.23.13" % Test
  )

  private val tapir = List(
    "com.softwaremill.sttp.tapir"   %% "tapir-core"         % "1.0.5",
    "com.softwaremill.sttp.tapir"   %% "tapir-openapi-docs" % "1.0.5" % Test,
    "com.softwaremill.sttp.apispec" %% "openapi-circe-yaml" % "0.2.1" % Test
  )

  private val fuuid = List(
    "io.chrisdavenport" %% "fuuid" % "0.8.0-M2"
  )

  private val scalacheck = List(
    "org.scalacheck" %% "scalacheck" % "1.16.0"
  )

  private val documentation = List(
    "org.typelevel"               %% "cats-effect" % "3.3.14",
    "io.circe"                    %% "circe-core"  % "0.14.2",
    "org.tpolecat"                %% "doobie-h2"   % "1.0.0-RC2",
    "org.http4s"                  %% "http4s-dsl"  % "0.23.13",
    "org.scalacheck"              %% "scalacheck"  % "1.16.0",
    "com.softwaremill.sttp.tapir" %% "tapir-core"  % "1.0.5",
    "io.chrisdavenport"           %% "fuuid"       % "0.8.0-M2"
  )

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = JvmPlugin

  override def projectSettings: List[Def.Setting[_]] =
    List(
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

  /** Wraps the value in a `List` if current scala version matches the one provided, otherwise returns `Nil`. */
  def on[A](major: Int, minor: Int)(a: A): Def.Initialize[List[A]] =
    Def.setting {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some(v) if v == (major, minor) => List(a)
        case _                              => Nil
      }
    }

}
