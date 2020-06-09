import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin

object dependencies extends AutoPlugin {

  // scala-steward:off
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
  // scala-steward:on

  private val common = Seq("org.specs2" %% "specs2-scalacheck" % "4.9.4" % Test)

  private val parallel = on(2, 13) {
    "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0" % Test
  }

  private val cats = Def.setting {
    Seq(
      "org.typelevel" %% "cats-effect"       % V.`cats-effect`.value % Provided,
      "org.typelevel" %% "cats-laws"         % "2.1.1"               % Test,
      "org.typelevel" %% "discipline-specs2" % "1.1.0"               % Test,
      "org.specs2"    %% "specs2-cats"       % "4.9.4"               % Test
    )
  }

  private val literal = Def.setting {
    Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided,
      "com.chuusai"   %% "shapeless"     % "2.3.3"            % Test
    )
  }

  private val doobie = Def.setting {
    Seq(
      "org.tpolecat" %% "doobie-core"   % V.doobie.value % Provided,
      "org.tpolecat" %% "doobie-specs2" % "0.9.0"        % Test,
      "org.tpolecat" %% "doobie-h2"     % "0.9.0"        % Test,
      "org.specs2"   %% "specs2-cats"   % "4.9.4"        % Test
    )
  }

  private val circe = Def.setting {
    Seq(
      "io.circe"      %% "circe-core"        % V.circe.value % Provided,
      "org.typelevel" %% "discipline-specs2" % "1.1.0"       % Test,
      "io.circe"      %% "circe-testing"     % "0.13.0"      % Test
    )
  }

  private val http4s = Def.setting {
    Seq(
      "org.http4s" %% "http4s-core" % V.http4s.value % Provided,
      "org.http4s" %% "http4s-dsl"  % "0.21.4"       % Test
    )
  }

  private val scalacheck = Seq(
    "org.scalacheck" %% "scalacheck" % V.scalacheck % Provided
  )

  private val documentation = Seq(
    "org.typelevel"  %% "cats-effect" % "2.1.3",
    "io.circe"       %% "circe-core"  % "0.13.0",
    "org.tpolecat"   %% "doobie-h2"   % "0.9.0",
    "org.http4s"     %% "http4s-dsl"  % "0.21.4",
    "org.scalacheck" %% "scalacheck"  % "1.14.3"
  )

  private val kotlin = Seq(
    "org.slf4j" % "slf4j-simple" % "1.6.2" % Provided,
    "io.github.microutils" % "kotlin-logging" % "1.7.9" % Provided,
    "com.novocode" % "junit-interface" % "0.11" % Test,
    "com.fasterxml.uuid" % "java-uuid-generator" % "4.0" % Test
  )

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = JvmPlugin

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      libraryDependencies ++= common ++ parallel.value,
      libraryDependencies ++= {
        projectID.value.name match {
          case "documentation"       => documentation
          case "microsite"           => documentation
          case "memeid4s-cats"       => cats.value
          case "memeid4s-literal"    => literal.value
          case "memeid4s-doobie"     => doobie.value
          case "memeid4s-circe"      => circe.value
          case "memeid4s-http4s"     => http4s.value
          case "memeid4s-scalacheck" => scalacheck
          case "memeid-kotlin"       => kotlin
          case _                     => Nil
        }
      }
    )

  /**
   * Wraps the value in a `Seq` if current scala version matches the one provided,
   * otherwise returns `Nil`.
   */
  def on[A](pf: PartialFunction[(Long, Long), String]): Def.Initialize[String] =
    Def.setting {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some(v) => pf(v)
        case _       => sys.error("Invalid Scala version")
      }
    }

  /**
   * Wraps the value in a `Seq` if current scala version matches the one provided,
   * otherwise returns `Nil`.
   */
  def on[A](major: Int, minor: Int)(a: A): Def.Initialize[Seq[A]] =
    Def.setting {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some(v) if v == (major, minor) => Seq(a)
        case _                              => Nil
      }
    }

}
