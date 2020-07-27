import play.core.PlayVersion
import play.sbt.PlayImport.ws
import sbt._

object AppDependencies {
  val compile = Seq(
    ws,
    "uk.gov.hmrc" %% "bootstrap-play-26" % "1.8.0"
  )
  val test = Seq(
    "org.scalatest" %% "scalatest" % "3.0.5",
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2",
    "org.pegdown" % "pegdown" % "1.6.0",
    "com.github.tomakehurst" % "wiremock-jre8" % "2.21.0",
    "com.typesafe.play" %% "play-test" % PlayVersion.current
  )
}