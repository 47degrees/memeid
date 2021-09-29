import sbt._

import sbt.Keys._
import sbt.plugins.JvmPlugin

object dependencies extends AutoPlugin {

  private val common = List("org.specs2" %% "specs2-scalacheck" % "4.12.1" % Test)

  private val parallel = on(2, 13) {
    "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4" % Test
  }

  private val cats = List(
    "org.typelevel" %% "cats-effect"       % "2.5.4",
    "org.typelevel" %% "cats-laws"         % "2.6.1"  % Test,
    "org.typelevel" %% "discipline-specs2" % "1.2.2"  % Test,
    "org.specs2"    %% "specs2-cats"       % "4.12.1" % Test
  )

  private val literal = Def.setting {
    Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided,
      "com.chuusai"   %% "shapeless"     % "2.3.7"            % Test
    )
  }

  private val doobie = List(
    "org.tpolecat" %% "doobie-core"   % "0.13.4",
    "org.tpolecat" %% "doobie-specs2" % "0.13.4" % Test,
    "org.tpolecat" %% "doobie-h2"     % "0.13.4" % Test,
    "org.specs2"   %% "specs2-cats"   % "4.12.1" % Test
  )

  private val circe = List(
    "io.circe"      %% "circe-core"        % "0.14.1",
    "org.typelevel" %% "discipline-specs2" % "1.2.2"  % Test,
    "io.circe"      %% "circe-testing"     % "0.14.1" % Test
  )

  private val http4s = List(
    "org.http4s" %% "http4s-core" % "0.22.5",
    "org.http4s" %% "http4s-dsl"  % "0.22.5" % Test
  )

  private val tapir = List(
    "com.softwaremill.sttp.tapir" %% "tapir-core"               % "0.18.3",
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % "0.18.3" % Test,
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % "0.18.3" % Test
  )

  private val fuuid = List(
    "io.chrisdavenport" %% "fuuid" % "0.7.0"
  )

  private val scalacheck = List(
    "org.scalacheck" %% "scalacheck" % "1.15.4"
  )

  private val documentation = List(
    "org.typelevel"               %% "cats-effect" % "2.5.4",
    "io.circe"                    %% "circe-core"  % "0.14.1",
    "org.tpolecat"                %% "doobie-h2"   % "0.13.4",
    "org.http4s"                  %% "http4s-dsl"  % "0.22.5",
    "org.scalacheck"              %% "scalacheck"  % "1.15.4",
    "com.softwaremill.sttp.tapir" %% "tapir-core"  % "0.18.3",
    "io.chrisdavenport"           %% "fuuid"       % "0.7.0"
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
