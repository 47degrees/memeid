ThisBuild / scalaVersion       := "2.13.1"
ThisBuild / crossScalaVersions := Seq("2.12.10", "2.13.1")
ThisBuild / organization       := "com.47deg"

Global / onChangedBuildSource := ReloadOnSourceChanges

addCommandAlias("ci-test", "fix --check; +docs/mdoc; +test")
addCommandAlias("ci-docs", "+docs/mdoc; headerCreateAll")

lazy val `root` = project
  .in(file("."))
  .aggregate(allProjects: _*)
  .settings(skip in publish := true)

lazy val `docs` = project
  .in(file("memeid-docs"))
  .enablePlugins(MdocPlugin)
  .settings(mdocVariables += "NAME" -> "memeid")
  .settings(mdocOut := file("."))
  .settings(skip in publish := true)
  .dependsOn(allProjects.map(ClasspathDependency(_, None)): _*)

lazy val `website` = project
  .aggregate(allProjects: _*)
  .enablePlugins(MdocPlugin)
  .enablePlugins(MicrositesPlugin)
  .settings(skip in publish := true)
  .settings(mdocIn := file("website/docs"))
  .dependsOn(allProjects.map(ClasspathDependency(_, None)): _*)

lazy val `memeid` = project
  .settings(crossPaths := false)
  .settings(publishMavenStyle := true)
  .settings(autoScalaLibrary := false)

lazy val memeid4s = project
  .dependsOn(`memeid`)
  .dependsOn(`memeid4s-scalacheck` % Test)

lazy val `memeid4s-cats` = project
  .dependsOn(`memeid4s`)
  .dependsOn(`memeid4s-scalacheck` % Test)

lazy val `memeid4s-literal` = project
  .dependsOn(`memeid4s`)

lazy val `memeid4s-doobie` = project
  .dependsOn(`memeid4s`)

lazy val `memeid4s-circe` = project
  .dependsOn(`memeid4s`)
  .dependsOn(`memeid4s-cats` % Test)
  .dependsOn(`memeid4s-scalacheck` % Test)

lazy val `memeid4s-http4s` = project
  .dependsOn(`memeid4s`)
  .dependsOn(`memeid4s-cats` % Test)
  .dependsOn(`memeid4s-scalacheck` % Test)

lazy val `memeid4s-scalacheck` = project
  .dependsOn(memeid)

lazy val allProjects: Seq[ProjectReference] = Seq(
  `memeid`,
  memeid4s,
  `memeid4s-cats`,
  `memeid4s-literal`,
  `memeid4s-doobie`,
  `memeid4s-circe`,
  `memeid4s-http4s`,
  `memeid4s-scalacheck`
)