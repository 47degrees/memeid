ThisBuild / scalaVersion       := "2.13.2"
ThisBuild / crossScalaVersions := Seq("2.12.11", "2.13.2")
ThisBuild / organization       := "com.47deg"

addCommandAlias("ci-test", "fix --check; +mdoc; +test")
addCommandAlias("ci-docs", "+mdoc; headerCreateAll")
addCommandAlias("ci-microsite", "website/publishMicrosite")

lazy val `docs` = (project in file(".docs"))
  .enablePlugins(MdocPlugin)
  .settings(mdocIn := file(".docs"))
  .settings(mdocOut := file("."))
  .settings(skip in publish := true)
  .dependsOn(allProjects: _*)

lazy val `website` = project
  .enablePlugins(MdocPlugin)
  .enablePlugins(MicrositesPlugin)
  .settings(skip in publish := true)
  .settings(mdocIn := file("website/docs"))
  .dependsOn(allProjects: _*)

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

lazy val allProjects: Seq[ClasspathDep[ProjectReference]] = Seq(
  `memeid`,
  memeid4s,
  `memeid4s-cats`,
  `memeid4s-literal`,
  `memeid4s-doobie`,
  `memeid4s-circe`,
  `memeid4s-http4s`,
  `memeid4s-scalacheck`
)
