ThisBuild / scalaVersion := "2.12.10"
ThisBuild / organization := "com.47deg"

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val root = project
  .in(file("."))
  .enablePlugins(MdocPlugin)
  .settings(skip in publish := true)
  .settings(dependencies.docs)
  .aggregate(allProjects: _*)
  .dependsOn(allProjects.map(ClasspathDependency(_, None)): _*)
  .settings(mdocOut := file("."))
  .settings(name := "memeid")

lazy val `memeid` = project
  .settings(crossPaths := false)
  .settings(autoScalaLibrary := false)

lazy val `memeid-scala` = project
  .dependsOn(`memeid`)
  .settings(dependencies.common)

lazy val `memeid-cats` = project
  .dependsOn(`memeid-scala` % "compile->compile;test->test")
  .settings(dependencies.common, dependencies.cats)
  .settings(dependencies.compilerPlugins)

lazy val `memeid-literal` = project
  .dependsOn(memeid)
  .settings(dependencies.common, dependencies.literal)

lazy val `memeid-doobie` = project
  .dependsOn(`memeid-cats`)
  .settings(dependencies.common, dependencies.doobie)

lazy val `memeid-circe` = project
  .dependsOn(`memeid-cats`)
  .settings(dependencies.common, dependencies.circe)

lazy val `memeid-http4s` = project
  .dependsOn(`memeid-cats` % "compile->compile;test->test")
  .settings(dependencies.common, dependencies.http4s)

lazy val allProjects: Seq[ProjectReference] = Seq(
  `memeid`,
  `memeid-scala`,
  `memeid-cats`,
  `memeid-literal`,
  `memeid-doobie`,
  `memeid-circe`,
  `memeid-http4s`
)
