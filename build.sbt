Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalaVersion := "2.12.10"

lazy val root = project
  .in(file("."))
  .settings(name := "memeid")
  .settings(
    libraryDependencies ++= Seq(
      //"org.typelevel" %% "cats-effect"       % "2.0.0",
      "org.specs2" %% "specs2-scalacheck" % "4.8.1" % Test,
      //"org.specs2"    %% "specs2-cats"       % "4.8.1" % Test,
      //"org.typelevel" %% "cats-laws"         % "2.1.0" % Test,
      "org.typelevel" %% "discipline-specs2" % "1.0.0" % Test
    )
  )

lazy val cats = project
  .in(file("cats"))
  .dependsOn(root)
  .settings(name := "memeid-cats")
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect"       % "2.0.0",
      "org.specs2"    %% "specs2-scalacheck" % "4.8.1" % Test,
      "org.specs2"    %% "specs2-cats"       % "4.8.1" % Test,
      "org.typelevel" %% "cats-laws"         % "2.1.0" % Test,
      "org.typelevel" %% "discipline-specs2" % "1.0.0" % Test
    )
  )

lazy val doobie = project
  .in(file("doobie"))
  .dependsOn(root)
  .settings(name := "memeid-doobie")
  .settings(
    libraryDependencies ++= Seq(
      "org.tpolecat" %% "doobie-core"   % "0.8.8",
      "org.tpolecat" %% "doobie-specs2" % "0.8.8" % Test,
      "org.specs2"   %% "specs2-cats"   % "4.8.1" % Test,
      "org.tpolecat" %% "doobie-h2"     % "0.8.8" % Test
    )
  )

lazy val circe = project
  .in(file("circe"))
  .dependsOn(root)
  .settings(name := "memeid-circe")
  .settings(
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % "0.12.3"
    )
  )
