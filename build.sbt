lazy val fullCrossScala = Seq(dependencies.scala2_12, dependencies.scala2_13, dependencies.scala3)

ThisBuild / scalaVersion := dependencies.scala3
ThisBuild / organization := "com.47deg"

addCommandAlias("ci-test", "fix --check; +mdoc; testCovered")
addCommandAlias("ci-docs", "github; documentation/mdoc; headerCreateAll; microsite3/publishMicrosite")
addCommandAlias("ci-publish", "github; ci-release")

////////////////
////  JAVA  ////
////////////////

lazy val memeid = projectMatrix
  .in(file("modules/memeid"))
  .settings(moduleName := "memeid")
  .settings(crossPaths := false)
  .settings(publishMavenStyle := true)
  .settings(autoScalaLibrary := false)
  .settings(dependencies.commonSettings)
  .jvmPlatform(autoScalaLibrary = false)

/////////////////
////  SCALA  ////
/////////////////

lazy val memeid4s = projectMatrix
  .in(file("modules/memeid4s"))
  .settings(moduleName := "memeid4s")
  .settings(dependencies.commonSettings)
  .dependsOn(memeid, `memeid4s-scalacheck` % Test)
  .jvmPlatform(scalaVersions = fullCrossScala)

lazy val `memeid4s-cats` = projectMatrix
  .in(file("modules/memeid4s-cats"))
  .settings(moduleName := "memeid4s-cats")
  .settings(dependencies.commonSettings)
  .settings(libraryDependencies ++= dependencies.cats)
  .dependsOn(`memeid4s`, `memeid4s-scalacheck` % Test)
  .jvmPlatform(scalaVersions = fullCrossScala)

lazy val `memeid4s-literal` = projectMatrix
  .in(file("modules/memeid4s-literal"))
  .settings(moduleName := "memeid4s-literal")
  .settings(dependencies.commonSettings)
  .settings(dependencies.literalSettings)
  .dependsOn(`memeid4s`)
  .jvmPlatform(scalaVersions = fullCrossScala)

lazy val `memeid4s-doobie` = projectMatrix
  .in(file("modules/memeid4s-doobie"))
  .settings(moduleName := "memeid4s-doobie")
  .settings(dependencies.commonSettings)
  .settings(libraryDependencies ++= dependencies.doobie)
  .dependsOn(`memeid4s`)
  .jvmPlatform(scalaVersions = fullCrossScala)

lazy val `memeid4s-circe` = projectMatrix
  .in(file("modules/memeid4s-circe"))
  .settings(moduleName := "memeid4s-circe")
  .settings(dependencies.commonSettings)
  .settings(libraryDependencies ++= dependencies.circe)
  .dependsOn(`memeid4s`, `memeid4s-cats` % Test, `memeid4s-scalacheck` % Test)
  .jvmPlatform(scalaVersions = fullCrossScala)

lazy val `memeid4s-http4s` = projectMatrix
  .in(file("modules/memeid4s-http4s"))
  .settings(moduleName := "memeid4s-http4s")
  .settings(dependencies.commonSettings)
  .settings(libraryDependencies ++= dependencies.http4s)
  .dependsOn(`memeid4s`, `memeid4s-cats` % Test, `memeid4s-scalacheck` % Test)
  .jvmPlatform(scalaVersions = fullCrossScala)

lazy val `memeid4s-tapir` = projectMatrix
  .in(file("modules/memeid4s-tapir"))
  .settings(moduleName := "memeid4s-tapir")
  .settings(dependencies.commonSettings)
  .settings(libraryDependencies ++= dependencies.tapir)
  .dependsOn(`memeid4s`, `memeid4s-cats` % Test, `memeid4s-scalacheck` % Test)
  .jvmPlatform(scalaVersions = fullCrossScala)

lazy val `memeid4s-fuuid` = projectMatrix
  .in(file("modules/memeid4s-fuuid"))
  .settings(moduleName := "memeid4s-fuuid")
  .settings(dependencies.commonSettings)
  .settings(libraryDependencies ++= dependencies.fuuid)
  .dependsOn(`memeid4s`, `memeid4s-scalacheck` % Test)
  .jvmPlatform(scalaVersions = fullCrossScala)

lazy val `memeid4s-scalacheck` = projectMatrix
  .in(file("modules/memeid4s-scalacheck"))
  .settings(moduleName := "memeid4s-scalacheck")
  .settings(dependencies.commonSettings)
  .settings(libraryDependencies ++= dependencies.scalacheck)
  .dependsOn(memeid)
  .jvmPlatform(scalaVersions = fullCrossScala)

//////////////////////////
//// MODULES REGISTRY ////
//////////////////////////

lazy val crossBuiltModules: Seq[sbt.internal.ProjectMatrix] = Seq(
  memeid, `memeid4s-scalacheck`, memeid4s, `memeid4s-cats`, `memeid4s-literal`, `memeid4s-doobie`, `memeid4s-circe`,
  `memeid4s-http4s`, `memeid4s-tapir`, `memeid4s-fuuid`
)

lazy val crossBuiltModuleDeps: Seq[
  sbt.internal.MatrixClasspathDep[sbt.internal.ProjectMatrixReference]
] =
  crossBuiltModules.map(m => sbt.internal.ProjectMatrix.MatrixClasspathDependency(m, configuration = None))

/////////////////////////
////  DOCUMENTATION  ////
/////////////////////////

lazy val documentation = projectMatrix
  .enablePlugins(MdocPlugin)
  .settings(dependencies.commonSettings)
  .settings(libraryDependencies ++= dependencies.documentation)
  .dependsOn(crossBuiltModuleDeps: _*)
  .settings(mdocOut := file("."))
  .settings(publish / skip := true)
  .jvmPlatform(scalaVersions = Seq(dependencies.scala2_13))

lazy val microsite = projectMatrix
  .enablePlugins(MdocPlugin)
  .enablePlugins(MicrositesPlugin)
  .settings(dependencies.commonSettings)
  .settings(libraryDependencies ++= dependencies.documentation)
  .settings(dependencies.micrositeSettings)
  .dependsOn(crossBuiltModuleDeps: _*)
  .settings(publish / skip := true)
  .jvmPlatform(scalaVersions = Seq(dependencies.scala3))

//////////////////////
////  BENCHMARKS  ////
//////////////////////

lazy val bench = projectMatrix
  .dependsOn(memeid4s)
  .enablePlugins(JmhPlugin)
  .disablePlugins(ScoverageSbtPlugin)
  .enablePlugins(HoodPlugin)
  /*
   * Not used but required by ScoverageSbtPlugin
   * [error] Runtime reference to undefined setting:
   * [error]
   * [error]   bench / Compile / coverageDataDir from coverageAggregate ((scoverage.ScoverageSbtPlugin.projectSettings) ScoverageSbtPlugin.scala:62)
   */
  .settings(
    Global / excludeLintKeys += coverageDataDir,
    coverageDataDir          := target.value / "test"
  )
  .settings(publish / skip := true)
  .jvmPlatform(scalaVersions = Seq(dependencies.scala3))

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
