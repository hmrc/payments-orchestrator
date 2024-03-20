import play.sbt.PlayImport.ws
import sbt.*

object AppDependencies {

  val boostrapVersion = "8.5.0"
  val compile = Seq(
    ws,
    "uk.gov.hmrc"                   %% "bootstrap-backend-play-30"  % boostrapVersion,
    "com.fasterxml.jackson.module"  %% "jackson-module-scala"       % "2.15.2"
  )
  val test = Seq(
    "org.scalatest"           %% "scalatest"                        % "3.2.17",
    "org.scalatestplus.play"  %% "scalatestplus-play"               % "5.1.0",
    "org.wiremock"            %  "wiremock-standalone"              % "3.2.0",
    "org.playframework"       %% "play-test"                        % "2.8.20",
    "com.vladsch.flexmark"    %  "flexmark-all"                     % "0.64.8",
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"           % boostrapVersion
  ).map(_ % Test)
}