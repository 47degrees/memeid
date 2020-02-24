import sbt.{Def, _}
import sbt.Keys._

object dependencies {

  object V {

    val cats = "2.1.1"

    val circe = "0.13.0"

    val doobie = "0.8.8"

    val http4s = "0.21.1"

    val scalacheck = "1.14.3"

  }

  val common: Seq[Def.Setting[Seq[ModuleID]]] = Seq(
    libraryDependencies += "org.specs2" %% "specs2-scalacheck" % "4.8.3" % Test,
    libraryDependencies ++= on(2, 13) {
      "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0" % Test
    }.value
  )

  val cats: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-effect"       % V.cats  % Provided,
    "org.typelevel" %% "cats-laws"         % "2.1.0" % Test,
    "org.typelevel" %% "discipline-specs2" % "1.0.0" % Test,
    "org.specs2"    %% "specs2-cats"       % "4.8.3" % Test
  )

  val literal: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided,
    "com.chuusai"    %% "shapeless"    % "2.3.3"            % Test
  )

  val doobie: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "org.tpolecat" %% "doobie-core"   % V.doobie % Provided,
    "org.tpolecat" %% "doobie-specs2" % "0.8.8"  % Test,
    "org.tpolecat" %% "doobie-h2"     % "0.8.8"  % Test,
    "org.specs2"   %% "specs2-cats"   % "4.8.3"  % Test
  )

  val circe: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "io.circe"      %% "circe-core"        % V.circe  % Provided,
    "org.typelevel" %% "discipline-specs2" % "1.0.0"  % Test,
    "io.circe"      %% "circe-testing"     % "0.13.0" % Test
  )

  val http4s: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "org.http4s" %% "http4s-core" % V.http4s % Provided,
    "org.http4s" %% "http4s-dsl"  % "0.21.1" % Test
  )

  val scalacheck: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "org.scalacheck" %% "scalacheck" % V.scalacheck % Provided
  )

  val docs: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "org.tpolecat" %% "doobie-h2"  % "0.8.8",
    "org.http4s"   %% "http4s-dsl" % "0.21.1"
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
