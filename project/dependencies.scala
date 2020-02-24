import sbt.{Def, _}
import sbt.Keys._

object dependencies {

  object V {

    val `cats-effect` = on {
      case (2, 12) => "[0.2,)"
      case (2, 13) => "[2.0.0,)"
    }

    val circe = on {
      case (2, 12) => "[0.6.0,)"
      case (2, 13) => "[0.12.0,)"
    }

    val doobie = on {
      case (2, 12) => "[0.4.0,)"
      case (2, 13) => "[0.8.2,)"
    }

    val http4s = on {
      case (2, 12) => "[0.15.0,)"
      case (2, 13) => "[0.21.0,)"
    }

    val scalacheck = "[1.14.0,)"

  }

  val common: Seq[Def.Setting[Seq[ModuleID]]] = Seq(
    libraryDependencies += "org.specs2" %% "specs2-scalacheck" % "4.8.3" % Test,
    libraryDependencies ++= on(2, 13) {
      "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0" % Test
    }.value
  )

  val cats: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-effect"       % V.`cats-effect`.value % Provided,
    "org.typelevel" %% "cats-laws"         % "2.1.0"               % Test,
    "org.typelevel" %% "discipline-specs2" % "1.0.0"               % Test,
    "org.specs2"    %% "specs2-cats"       % "4.8.3"               % Test
  )

  val literal: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided,
    "com.chuusai"    %% "shapeless"    % "2.3.3"            % Test
  )

  val doobie: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "org.tpolecat" %% "doobie-core"   % V.doobie.value % Provided,
    "org.tpolecat" %% "doobie-specs2" % "0.8.8"        % Test,
    "org.tpolecat" %% "doobie-h2"     % "0.8.8"        % Test,
    "org.specs2"   %% "specs2-cats"   % "4.8.3"        % Test
  )

  val circe: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "io.circe"      %% "circe-core"        % V.circe.value % Provided,
    "org.typelevel" %% "discipline-specs2" % "1.0.0"       % Test,
    "io.circe"      %% "circe-testing"     % "0.13.0"      % Test
  )

  val http4s: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "org.http4s" %% "http4s-core" % V.http4s.value % Provided,
    "org.http4s" %% "http4s-dsl"  % "0.21.1"       % Test
  )

  val scalacheck: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "org.scalacheck" %% "scalacheck" % V.scalacheck % Provided
  )

  val docs: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "org.typelevel"  %% "cats-effect" % "2.1.1",
    "io.circe"       %% "circe-core"  % "0.13.0",
    "org.tpolecat"   %% "doobie-h2"   % "0.8.8",
    "org.http4s"     %% "http4s-dsl"  % "0.21.1",
    "org.scalacheck" %% "scalacheck"  % "1.14.3"
  )

  /**
   * Wraps the value in a `Seq` if current scala version matches the one provided,
   * otherwise returns `Nil`.
   */
  def on[A](pf: PartialFunction[(Long, Long), String]): Def.Initialize[String] = Def.setting {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some(v) => pf(v)
      case _       => sys.error("Invalid Scala version")
    }
  }

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
