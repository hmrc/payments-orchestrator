import play.sbt.PlayImport.ws
import sbt.*

object AppDependencies {

  val boostrapVersion = "7.22.0"
  val compile = Seq(
    ws,
    "uk.gov.hmrc"                   %% "bootstrap-backend-play-28"  % boostrapVersion,
    "com.fasterxml.jackson.module"  %% "jackson-module-scala"       % "2.14.2"
  )
  val test = Seq(
    "org.scalatest"           %% "scalatest"                        % "3.2.15",
    "org.scalatestplus.play"  %% "scalatestplus-play"               % "5.1.0",
    "com.github.tomakehurst"  %  "wiremock-jre8"                    % "2.35.0",
    "com.typesafe.play"       %% "play-test"                        % "2.8.18",
    "com.vladsch.flexmark"    %  "flexmark-all"                     % "0.64.6",
    "uk.gov.hmrc"             %% "bootstrap-test-play-28"           % boostrapVersion
  ).map(_ % Test)
}