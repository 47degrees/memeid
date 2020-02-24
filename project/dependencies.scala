import sbt.{Def, _}
import sbt.Keys._

object dependencies {

  object V {

    val cats                = "2.1.1"
    val `cats-laws`         = "2.1.0"
    val circe               = "0.13.0"
    val `discipline-specs2` = "1.0.0"
    val doobie              = "0.8.8"
    val http4s              = "0.21.1"
    val specs               = "4.8.3"
    val shapeless           = "2.3.3"
    val `par-colls`         = "0.2.0"

  }

  val common: Seq[Def.Setting[Seq[ModuleID]]] = Seq(
    libraryDependencies += "org.specs2" %% "specs2-scalacheck" % V.specs % Test,
    libraryDependencies ++= on(2, 13) {
      "org.scala-lang.modules" %% "scala-parallel-collections" % V.`par-colls` % Test
    }.value
  )

  val cats: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-effect"       % V.cats,
    "org.typelevel" %% "cats-laws"         % V.`cats-laws` % Test,
    "org.typelevel" %% "discipline-specs2" % V.`discipline-specs2` % Test,
    "org.specs2"    %% "specs2-cats"       % V.specs % Test
  )

  val literal: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided,
    "com.chuusai"    %% "shapeless"    % V.shapeless        % Test
  )

  val doobie: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "org.tpolecat" %% "doobie-core"   % V.doobie,
    "org.tpolecat" %% "doobie-specs2" % V.doobie % Test,
    "org.tpolecat" %% "doobie-h2"     % V.doobie % Test,
    "org.specs2"   %% "specs2-cats"   % V.specs % Test
  )

  val circe: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "io.circe"      %% "circe-core"        % V.circe,
    "org.typelevel" %% "discipline-specs2" % V.`discipline-specs2` % Test,
    "io.circe"      %% "circe-testing"     % V.circe % Test
  )

  val http4s: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "org.http4s" %% "http4s-core" % V.http4s,
    "org.http4s" %% "http4s-dsl"  % V.http4s % Test
  )

  val scalacheck: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "org.scalacheck" %% "scalacheck" % "1.14.3"
  )

  val docs: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "org.tpolecat" %% "doobie-h2"  % V.doobie,
    "org.http4s"   %% "http4s-dsl" % V.http4s
  )

  /**
   * Wraps the value in a `Seq` if current scala version matches the one provided,
   * otherwise returns `Nil`.
   */
  def on[A](major: Int, minor: Int)(a: A): Def.Initialize[Seq[A]] = Def.setting {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some(v) if v == (major, minor) => Seq(a)
      case _                              => Nil
    }
  }

}
