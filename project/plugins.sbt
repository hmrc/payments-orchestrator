resolvers += "hmrc-releases" at "https://artefacts.tax.service.gov.uk/artifactory/hmrc-releases/"
resolvers += "Typesafe Releases" at "https://repo.typesafe.com/typesafe/releases/"
resolvers += MavenRepository("HMRC-open-artefacts-maven2", "https://open.artefacts.tax.service.gov.uk/maven2")
resolvers += Resolver.url("HMRC-open-artefacts-ivy2", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(Resolver.ivyStylePatterns)

addSbtPlugin("uk.gov.hmrc"            % "sbt-auto-build"          % "3.13.0")
addSbtPlugin("uk.gov.hmrc"            % "sbt-distributables"      % "2.2.0")
addSbtPlugin("com.typesafe.play"      % "sbt-plugin"              % "2.8.20")
addSbtPlugin("org.scoverage"          % "sbt-scoverage"           % "1.8.2")
addSbtPlugin("org.wartremover"        % "sbt-wartremover"         % "3.1.3")
addSbtPlugin("org.scalariform"        % "sbt-scalariform"         % "1.8.3")
addSbtPlugin("org.scalastyle"         %% "scalastyle-sbt-plugin"  % "1.0.0")
addSbtPlugin("com.timushev.sbt"       % "sbt-updates"             % "0.6.3")
addDependencyTreePlugin