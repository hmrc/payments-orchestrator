import play.core.PlayVersion
import play.sbt.PlayImport.ws
import sbt._

object AppDependencies {
  val compile = Seq(
    ws,
    "uk.gov.hmrc" %% "bootstrap-backend-play-28" % "5.3.0"
    //"uk.gov.hmrc"    %% "play-frontend-govuk"                        % "0.77.0-play-28",
    // "uk.gov.hmrc"    %% "play-frontend-hmrc"                         % "0.74.0-play-28"
  )
  val test = Seq(
    "org.scalatest" %% "scalatest" % "3.1.0",
    "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0",
    "org.pegdown" % "pegdown" % "1.6.0",
    "com.github.tomakehurst" % "wiremock-jre8" % "2.27.2",
    "com.typesafe.play" %% "play-test" % "2.8.8",
    "com.vladsch.flexmark" % "flexmark-all" % "0.35.10"
  )
}