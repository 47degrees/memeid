ThisBuild / scalaVersion := "2.12.10"
ThisBuild / organization := "com.47deg"

Global / onChangedBuildSource := ReloadOnSourceChanges

addCommandAlias("ci-test", "fix --check; mdoc; test")
addCommandAlias("ci-docs", "mdoc; headerCreateAll")

lazy val `memeid-root` = project
  .in(file("."))
  .aggregate(allProjects: _*)
  .dependsOn(allProjects.map(ClasspathDependency(_, None)): _*)
  .enablePlugins(MdocPlugin)
  .settings(skip in publish := true)
  .settings(mdocOut := file("."))
  .settings(dependencies.docs)

lazy val `memeid` = project
  .settings(crossPaths := false)
  .settings(publishMavenStyle := true)
  .settings(autoScalaLibrary := false)
  .settings(dependencies.common)

lazy val memeid4s = project
  .dependsOn(`memeid` % "compile->compile;test->test")
  .settings(dependencies.common)

lazy val `memeid4s-cats` = project
  .dependsOn(memeid4s % "compile->compile;test->test")
  .settings(dependencies.common, dependencies.cats)
  .settings(dependencies.compilerPlugins)

lazy val `memeid4s-literal` = project
  .dependsOn(memeid)
  .settings(dependencies.common, dependencies.literal)

lazy val `memeid4s-doobie` = project
  .dependsOn(`memeid4s-cats`)
  .settings(dependencies.common, dependencies.doobie)

lazy val `memeid4s-circe` = project
  .dependsOn(`memeid4s-cats`)
  .settings(dependencies.common, dependencies.circe)

lazy val `memeid4s-http4s` = project
  .dependsOn(`memeid4s-cats` % "compile->compile;test->test")
  .settings(dependencies.common, dependencies.http4s)

lazy val allProjects: Seq[ProjectReference] = Seq(
  `memeid`,
  memeid4s,
  `memeid4s-cats`,
  `memeid4s-literal`,
  `memeid4s-doobie`,
  `memeid4s-circe`,
  `memeid4s-http4s`
)
