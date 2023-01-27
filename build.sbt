lazy val fullCrossScala = Seq(dependencies.scala2_12, dependencies.scala2_13, dependencies.scala3)

ThisBuild / scalaVersion       := dependencies.scala2_13
ThisBuild / crossScalaVersions := Seq(dependencies.scala2_12, dependencies.scala2_13)
ThisBuild / organization       := "com.47deg"

addCommandAlias("ci-test", "fix --check; testCovered")
addCommandAlias("ci-docs", "github; mdoc; headerCreateAll; publishMicrosite")
addCommandAlias("ci-publish", "github; ci-release")

////////////////
////  JAVA  ////
////////////////

lazy val `memeid` = module
  .settings(crossPaths := false)
  .settings(publishMavenStyle := true)
  .settings(autoScalaLibrary := false)
  .settings(scalaVersion := dependencies.scala3)

/////////////////
////  SCALA  ////
/////////////////

lazy val memeid4s = module
  .dependsOn(`memeid`, `memeid4s-scalacheck` % Test)
  .settings(
    scalaVersion       := dependencies.scala3,
    crossScalaVersions := fullCrossScala
  )

lazy val `memeid4s-cats` = module
  .dependsOn(`memeid4s`, `memeid4s-scalacheck` % Test)
  .settings(
    scalaVersion       := dependencies.scala3,
    crossScalaVersions := fullCrossScala
  )

lazy val `memeid4s-literal` = module
  .dependsOn(`memeid4s`)
  .settings(
    // Macros
    scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, major)) if major <= 12 =>
        Seq()
      case _ =>
        Seq("-Ytasty-reader")
    }),
    scalaVersion       := dependencies.scala2_13,
    crossScalaVersions := Seq(dependencies.scala2_12, dependencies.scala2_13)
  )

lazy val `memeid4s-doobie` = module
  .dependsOn(`memeid4s`)
  .settings(
    scalaVersion       := dependencies.scala3,
    crossScalaVersions := fullCrossScala
  )

lazy val `memeid4s-circe` = module
  .dependsOn(`memeid4s`, `memeid4s-cats` % Test, `memeid4s-scalacheck` % Test)
  .settings(
    scalaVersion       := dependencies.scala3,
    crossScalaVersions := fullCrossScala
  )

lazy val `memeid4s-http4s` = module
  .dependsOn(`memeid4s`, `memeid4s-cats` % Test, `memeid4s-scalacheck` % Test)
  .settings(
    scalaVersion       := dependencies.scala3,
    crossScalaVersions := fullCrossScala
  )

lazy val `memeid4s-tapir` = module
  .dependsOn(`memeid4s`, `memeid4s-cats` % Test, `memeid4s-scalacheck` % Test)
  .settings(
    scalaVersion       := dependencies.scala3,
    crossScalaVersions := fullCrossScala
  )

lazy val `memeid4s-fuuid` = module
  .dependsOn(`memeid4s`, `memeid4s-scalacheck` % Test)
  .settings(
    scalaVersion       := dependencies.scala3,
    crossScalaVersions := fullCrossScala
  )

lazy val `memeid4s-scalacheck` = module
  .dependsOn(memeid)
  .settings(
    scalaVersion       := dependencies.scala3,
    crossScalaVersions := fullCrossScala
  )

/////////////////////////
////  DOCUMENTATION  ////
/////////////////////////

//lazy val documentation = project
//  .enablePlugins(MdocPlugin)
//  .settings(mdocOut := file("."))
//  .dependsOn(allModules: _*)
//  .settings(
//    scalaVersion       := dependencies.scala2_13,
//    crossScalaVersions := Seq(dependencies.scala2_13)
//  )
//
//lazy val microsite = project
//  .enablePlugins(MdocPlugin)
//  .enablePlugins(MicrositesPlugin)
//  .dependsOn(allModules: _*)
//  .settings(
//    scalaVersion       := dependencies.scala2_13,
//    crossScalaVersions := Seq(dependencies.scala2_13)
//  )

//////////////////////
////  BENCHMARKS  ////
//////////////////////

//lazy val bench = project
//  .dependsOn(memeid4s)
//  .enablePlugins(JmhPlugin)
//  .disablePlugins(ScoverageSbtPlugin)
//  .enablePlugins(HoodPlugin)
//  /*
//   * Not used but required by ScoverageSbtPlugin
//   * [error] Runtime reference to undefined setting:
//   * [error]
//   * [error]   bench / Compile / coverageDataDir from coverageAggregate ((scoverage.ScoverageSbtPlugin.projectSettings) ScoverageSbtPlugin.scala:62)
//   */
//  .settings(
//    scalaVersion             := dependencies.scala2_13,
//    crossScalaVersions       := Seq(dependencies.scala2_13),
//    Global / excludeLintKeys += coverageDataDir,
//    coverageDataDir          := target.value / "test"
//  )

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
