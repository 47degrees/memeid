Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalaVersion := "2.12.10"

lazy val root = project
  .in(file("."))
  .enablePlugins(MdocPlugin)
  .settings(skip in publish := true)
  .settings(dependencies.docs)
  .aggregate(allProjects: _*)
  .dependsOn(allProjects.map(ClasspathDependency(_, None)): _*)
  .settings(
    mdocVariables := Map(
      "VERSION" -> version.value
    )
  )

lazy val `memeid` = project
  .settings(dependencies.common)

lazy val `memeid-cats` = project
  .dependsOn(`memeid` % "compile->compile;test->test")
  .settings(dependencies.common, dependencies.cats)
  .settings(dependencies.compilerPlugins)

lazy val `memeid-literal` = project
  .dependsOn(`memeid`)
  .settings(dependencies.common, dependencies.literal)

lazy val `memeid-doobie` = project
  .dependsOn(`memeid-cats`)
  .settings(dependencies.common, dependencies.doobie)

lazy val `memeid-circe` = project
  .dependsOn(`memeid-cats`)
  .settings(dependencies.common, dependencies.circe)

lazy val `memeid-http4s` = project
  .dependsOn(`memeid-cats`)
  .settings(dependencies.common, dependencies.http4s)

lazy val allProjects: Seq[ProjectReference] =
  Seq(`memeid`, `memeid-cats`, `memeid-literal`, `memeid-doobie`, `memeid-circe`, `memeid-http4s`)
