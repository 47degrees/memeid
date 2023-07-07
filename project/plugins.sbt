ThisBuild / libraryDependencySchemes ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
)
addSbtPlugin("com.eed3si9n"                      % "sbt-projectmatrix"        % "0.9.0")
addSbtPlugin("ch.epfl.scala"                     % "sbt-scalafix"             % "0.11.0")
addSbtPlugin("com.47deg"                         % "sbt-microsites"           % "1.4.3")
addSbtPlugin("com.alejandrohdezma"               % "sbt-codecov"              % "0.2.1")
addSbtPlugin("com.alejandrohdezma"               % "sbt-fix"                  % "0.7.1")
addSbtPlugin("com.alejandrohdezma"               % "sbt-github-header"        % "0.11.9")
addSbtPlugin("com.alejandrohdezma"               % "sbt-github-mdoc"          % "0.11.9")
addSbtPlugin("com.alejandrohdezma"               % "sbt-mdoc-toc"             % "0.4.1")
addSbtPlugin("com.alejandrohdezma"               % "sbt-remove-test-from-pom" % "0.1.0")
addSbtPlugin("com.alejandrohdezma"               % "sbt-scalafmt-defaults"    % "0.9.0")
addSbtPlugin("com.github.sbt"                    % "sbt-ci-release"           % "1.5.12")
addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings"         % "3.0.2")
addSbtPlugin("de.heikoseeberger"                 % "sbt-header"               % "5.10.0")
addSbtPlugin("io.github.davidgregory084"         % "sbt-tpolecat"             % "0.4.3")
addSbtPlugin("org.scalameta"                     % "sbt-mdoc"                 % "2.3.7")
addSbtPlugin("org.scalameta"                     % "sbt-scalafmt"             % "2.5.0")
addSbtPlugin("org.scoverage"                     % "sbt-scoverage"            % "2.0.8")
addSbtPlugin("pl.project13.scala"                % "sbt-jmh"                  % "0.4.5")
addSbtPlugin("com.47deg"                        %% "sbt-hood-plugin"          % "0.4.0")
