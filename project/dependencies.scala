import sbt._
import sbt.Keys._

object dependencies {

  val V = new {

    val cats                = "2.0.0"
    val circe               = "0.12.3"
    val `discipline-specs2` = "1.0.0"
    val doobie              = "0.8.8"
    val specs               = "4.8.1"
    val shapeless           = "2.3.3"

  }

  val common: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "org.specs2" %% "specs2-scalacheck" % V.specs % Test
  )

  val cats: Def.Setting[Seq[ModuleID]] = libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-effect"       % V.cats,
    "org.typelevel" %% "cats-laws"         % V.cats % Test,
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
    "io.circe" %% "circe-core" % V.circe
  )

}
