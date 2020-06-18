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

/////////////////
////  KOTLIN ////
/////////////////

lazy val `memeid-kotlin` = module
  .dependsOn(`memeid`)
  .disablePlugins(ScoverageSbtPlugin)
  .settings(crossPaths := false)
  .settings(skip in publish := true)
  .settings(publishMavenStyle := true)
  .settings(autoScalaLibrary := false)
  .settings(kotlinLib("stdlib"))
  .settings(kotlincOptions += "-verbose")

lazy val bench = project
  .dependsOn(memeid4s)
  .enablePlugins(JmhPlugin)
  .disablePlugins(ScoverageSbtPlugin)
  .enablePlugins(HoodPlugin)

val runAvgtimeCmd =
  "bench/jmh:run -i 15 -wi 15 -bm AverageTime -tu ns"

val runThroughputCmd =
  "bench/jmh:run -i 15 -wi 15 -bm Throughput -tu s"

addCommandAlias(
  "runAvgtime",
  ";" +
    runAvgtimeCmd +
    " -rff current.avgtime.csv;"
)
addCommandAlias(
  "runAvgtimeProf",
  ";" +
    runAvgtimeCmd +
    " -rff current.avgtime.prof.csv" +
    " -prof stack;"
)
addCommandAlias(
  "runThroughput",
  ";" +
    runAvgtimeCmd +
    " -rff current.throughput.csv;"
)
addCommandAlias(
  "runThroughputProf",
  ";" +
    runAvgtimeCmd +
    " -rff current.throughput.prof.csv" +
    " -prof stack;"
)
