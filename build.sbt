ThisBuild / scalaVersion       := "2.13.2"
ThisBuild / crossScalaVersions := Seq("2.12.11", "2.13.2")
ThisBuild / organization       := "com.47deg"

addCommandAlias("ci-test", "fix --check; +mdoc; testCovered")
addCommandAlias("ci-docs", "github; mdoc; headerCreateAll; publishMicrosite")
addCommandAlias("ci-publish", "github; ci-release")

lazy val documentation = project
  .enablePlugins(MdocPlugin)
  .settings(mdocOut := file("."))
  .dependsOn(allModules: _*)

lazy val microsite = project
  .enablePlugins(MdocPlugin)
  .enablePlugins(MicrositesPlugin)
  .dependsOn(allModules: _*)

////////////////
////  JAVA  ////
////////////////

lazy val `memeid` = module
  .settings(crossPaths := false)
  .settings(publishMavenStyle := true)
  .settings(autoScalaLibrary := false)

/////////////////
////  SCALA  ////
/////////////////

lazy val memeid4s              = module.dependsOn(`memeid`, `memeid4s-scalacheck` % Test)
lazy val `memeid4s-cats`       = module.dependsOn(`memeid4s`, `memeid4s-scalacheck` % Test)
lazy val `memeid4s-literal`    = module.dependsOn(`memeid4s`)
lazy val `memeid4s-doobie`     = module.dependsOn(`memeid4s`)
lazy val `memeid4s-circe`      = module.dependsOn(`memeid4s`, `memeid4s-cats` % Test, `memeid4s-scalacheck` % Test)
lazy val `memeid4s-http4s`     = module.dependsOn(`memeid4s`, `memeid4s-cats` % Test, `memeid4s-scalacheck` % Test)
lazy val `memeid4s-scalacheck` = module.dependsOn(memeid)
