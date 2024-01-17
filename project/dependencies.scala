import sbt._

import sbt.Keys._
import sbt.plugins.JvmPlugin

import mdoc.MdocPlugin.autoImport._

object dependencies extends AutoPlugin {

  val scala2_12 = "2.12.18"

  val scala2_13 = "2.13.12"

  val scala3 = "3.3.0"

  val commonSettings: Seq[Def.Setting[_]] = Seq(
    libraryDependencies ++= Seq(
      "org.specs2" %% "specs2-scalacheck" % "4.20.4" % Test
    ) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, major)) if major <= 12 =>
        Seq()
      case _ =>
        Seq("org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4" % Test)
    })
  )

  val cats: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-effect"       % "3.5.2",
    "org.typelevel" %% "cats-laws"         % "2.10.0" % Test,
    "org.typelevel" %% "discipline-specs2" % "1.4.0"  % Test,
    "org.specs2"    %% "specs2-cats"       % "4.20.4" % Test
  )

  val literalSettings: Seq[Def.Setting[_]] = Seq(
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29" % Test
    ) ++
      (CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, _)) =>
          Seq(
            "org.scala-lang" % "scala-reflect" % scalaVersion.value
          )
        case _ => Seq.empty
      })
  )

  val doobie: Seq[ModuleID] = Seq(
    "org.tpolecat" %% "doobie-core"  % "1.0.0-RC5",
    "org.tpolecat" %% "doobie-h2"    % "1.0.0-RC5",
    "org.tpolecat" %% "doobie-munit" % "1.0.0-RC5" % Test
  )

  val circe: Seq[ModuleID] = Seq(
    "io.circe"      %% "circe-core"        % "0.14.6",
    "org.typelevel" %% "discipline-specs2" % "1.4.0"  % Test,
    "io.circe"      %% "circe-testing"     % "0.14.6" % Test
  )

  val http4s: Seq[ModuleID] = Seq(
    "org.http4s" %% "http4s-core" % "0.23.25",
    "org.http4s" %% "http4s-dsl"  % "0.23.25" % Test
  )

  val tapir: Seq[ModuleID] = Seq(
    "com.softwaremill.sttp.tapir"   %% "tapir-core"         % "1.9.6",
    "com.softwaremill.sttp.tapir"   %% "tapir-openapi-docs" % "1.9.6" % Test,
    "com.softwaremill.sttp.apispec" %% "openapi-circe-yaml" % "0.7.3" % Test
  )

  val fuuid: Seq[ModuleID] = Seq(
    "io.chrisdavenport" %% "fuuid" % "0.8.0-M2"
  )

  val scalacheck: Seq[ModuleID] = Seq(
    "org.scalacheck" %% "scalacheck" % "1.17.0"
  )

  val documentation: Seq[ModuleID] = Seq(
    "org.typelevel"               %% "cats-effect" % "3.5.2",
    "io.circe"                    %% "circe-core"  % "0.14.6",
    "org.tpolecat"                %% "doobie-h2"   % "1.0.0-RC5",
    "org.http4s"                  %% "http4s-dsl"  % "0.23.25",
    "org.scalacheck"              %% "scalacheck"  % "1.17.0",
    "com.softwaremill.sttp.tapir" %% "tapir-core"  % "1.9.6",
    "io.chrisdavenport"           %% "fuuid"       % "0.8.0-M2"
  )

  val micrositeSettings: Seq[Def.Setting[_]] = Seq(
    mdocIn             := (Compile / sourceDirectory).value / "docs",
    mdocExtraArguments := Seq("--no-link-hygiene")
  )

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = JvmPlugin

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      Compile / compileOrder := CompileOrder.JavaThenScala
    )

}
