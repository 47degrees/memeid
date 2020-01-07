Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalaVersion := "2.12.10"

lazy val root = project
  .in(file("."))
  .settings(name := "memeid")
  .aggregate(core, cats, literal, doobie, circe)
  .settings(skip in publish := true)

lazy val core = project
  .settings(name := "memeid")
  .settings(dependencies.common)

lazy val cats = project
  .dependsOn(core % "compile->compile;test->test")
  .settings(name := "memeid-cats")
  .settings(dependencies.common, dependencies.cats)
  .settings(dependencies.compilerPlugins)

lazy val literal = project
  .dependsOn(core)
  .settings(name := "memeid-literal")
  .settings(dependencies.common, dependencies.literal)

lazy val doobie = project
  .dependsOn(cats)
  .settings(name := "memeid-doobie")
  .settings(dependencies.common, dependencies.doobie)

lazy val circe = project
  .dependsOn(cats)
  .settings(name := "memeid-circe")
  .settings(dependencies.common, dependencies.circe)

lazy val http4s = project
  .dependsOn(cats)
  .settings(name := "memeid-http4s")
  .settings(dependencies.common, dependencies.http4s)
