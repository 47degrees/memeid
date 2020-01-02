Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalaVersion := "2.12.10"

lazy val root = project
  .in(file("."))
  .settings(name := "memeid")
  .aggregate(core, cats, literal, doobie, circe)
  .settings(skip in publish := true)

lazy val core = project
  .dependsOn(node, time)
  .settings(name := "memeid")
  .settings(dependencies.common)

lazy val bits = project
  .settings(name := "memeid-bits")
  .settings(skip in publish := true)
  .settings(dependencies.common)

lazy val digest = project
  .settings(name := "memeid-digest")
  .settings(skip in publish := true)
  .settings(dependencies.common)

lazy val node = project
  .dependsOn(bits, digest)
  .settings(name := "memeid-node")
  .settings(skip in publish := true)
  .settings(dependencies.common)

lazy val time = project
  .settings(name := "memeid-time")
  .settings(skip in publish := true)

lazy val cats = project
  .dependsOn(core % "compile->compile;test->test")
  .settings(name := "memeid-cats")
  .settings(dependencies.common, dependencies.cats)

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
