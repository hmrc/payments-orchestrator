import play.sbt.PlayImport.ws
import sbt.*

object AppDependencies {

  val boostrapVersion = "9.19.0"
  val compile: Seq[ModuleID] = Seq(
    ws,
    "uk.gov.hmrc"                   %% "bootstrap-backend-play-30"  % boostrapVersion,
    "com.fasterxml.jackson.module"  %% "jackson-module-scala"       % "2.21.0"
  )
  val test: Seq[ModuleID] = Seq(
    "org.scalatest"           %% "scalatest"                        % "3.2.19",
    "org.scalatestplus.play"  %% "scalatestplus-play"               % "7.0.2",
    "org.wiremock"            %  "wiremock-standalone"              % "3.13.2",
    "org.playframework"       %% "play-test"                        % "3.0.10",
    "com.vladsch.flexmark"    %  "flexmark-all"                     % "0.64.8",
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"           % boostrapVersion
  ).map(_ % Test)
}