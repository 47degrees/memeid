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
  .settings(libraryDependencies += "org.specs2" %% "specs2-scalacheck" % "4.8.1" % Test)

lazy val bits = project
  .settings(name := "memeid-bits")
  .settings(skip in publish := true)
  .settings(libraryDependencies += "org.specs2" %% "specs2-scalacheck" % "4.8.1" % Test)

lazy val digest = project
  .settings(name := "memeid-digest")
  .settings(skip in publish := true)
  .settings(libraryDependencies += "org.specs2" %% "specs2-scalacheck" % "4.8.1" % Test)

lazy val node = project
  .dependsOn(bits, digest)
  .settings(name := "memeid-node")
  .settings(skip in publish := true)
  .settings(libraryDependencies += "org.specs2" %% "specs2-scalacheck" % "4.8.1" % Test)

lazy val time = project
  .settings(name := "memeid-time")
  .settings(skip in publish := true)

lazy val cats = project
  .dependsOn(core % "compile->compile;test->test")
  .settings(name := "memeid-cats")
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect"       % "2.0.0",
      "org.specs2"    %% "specs2-scalacheck" % "4.8.1" % Test,
      "org.specs2"    %% "specs2-cats"       % "4.8.1" % Test,
      "org.typelevel" %% "cats-laws"         % "2.1.0" % Test,
      "org.typelevel" %% "discipline-specs2" % "1.0.0" % Test
    )
  )

lazy val literal = project
  .dependsOn(core)
  .settings(name := "memeid-literal")
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided,
      "com.chuusai"    %% "shapeless"    % "2.3.3"            % Test,
      "org.specs2"     %% "specs2-core"  % "4.8.1"            % Test
    )
  )

lazy val doobie = project
  .dependsOn(cats)
  .settings(name := "memeid-doobie")
  .settings(
    libraryDependencies ++= Seq(
      "org.tpolecat" %% "doobie-core"   % "0.8.8",
      "org.tpolecat" %% "doobie-specs2" % "0.8.8" % Test,
      "org.specs2"   %% "specs2-cats"   % "4.8.1" % Test,
      "org.tpolecat" %% "doobie-h2"     % "0.8.8" % Test
    )
  )

lazy val circe = project
  .dependsOn(cats)
  .settings(name := "memeid-circe")
  .settings(
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % "0.12.3"
    )
  )
