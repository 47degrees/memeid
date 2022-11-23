ThisBuild / libraryDependencySchemes ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
)
addSbtPlugin("ch.epfl.scala"                     % "sbt-scalafix"             % "0.10.4")
addSbtPlugin("com.47deg"                         % "sbt-microsites"           % "1.3.2")
addSbtPlugin("com.alejandrohdezma"               % "sbt-codecov"              % "0.2.1")
addSbtPlugin("com.alejandrohdezma"               % "sbt-fix"                  % "0.7.0")
addSbtPlugin("com.alejandrohdezma"               % "sbt-github-header"        % "0.11.6")
addSbtPlugin("com.alejandrohdezma"               % "sbt-github-mdoc"          % "0.11.6")
addSbtPlugin("com.alejandrohdezma"               % "sbt-mdoc-toc"             % "0.4.0")
addSbtPlugin("com.alejandrohdezma"               % "sbt-modules"              % "0.2.0")
addSbtPlugin("com.alejandrohdezma"               % "sbt-remove-test-from-pom" % "0.1.0")
addSbtPlugin("com.alejandrohdezma"               % "sbt-scalafix-defaults"    % "0.10.0")
addSbtPlugin("com.alejandrohdezma"               % "sbt-scalafmt-defaults"    % "0.8.0")
addSbtPlugin("com.github.sbt"                    % "sbt-ci-release"           % "1.5.10")
addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings"         % "3.0.2")
addSbtPlugin("de.heikoseeberger"                 % "sbt-header"               % "5.9.0")
addSbtPlugin("io.github.davidgregory084"         % "sbt-tpolecat"             % "0.4.1")
addSbtPlugin("org.scalameta"                     % "sbt-mdoc"                 % "2.3.6")
addSbtPlugin("org.scalameta"                     % "sbt-scalafmt"             % "2.5.0")
addSbtPlugin("org.scoverage"                     % "sbt-scoverage"            % "2.0.6")
addSbtPlugin("pl.project13.scala"                % "sbt-jmh"                  % "0.4.3")
addSbtPlugin("com.47deg"                        %% "sbt-hood-plugin"          % "0.3.0")
