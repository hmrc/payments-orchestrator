import play.core.PlayVersion
import play.sbt.PlayImport.ws
import sbt._
object AppDependencies {
  val compile = Seq(
    ws,
    "uk.gov.hmrc" %% "simple-reactivemongo" % "7.27.0-play-26",
    "uk.gov.hmrc" %% "bootstrap-play-26" % "1.8.0",
    "uk.gov.hmrc" %% "order-id-encoder" % "1.1.0",
    "com.beachape" %% "enumeratum" % "1.6.0",
    "uk.gov.hmrc" %% "auth-client" % "3.0.0-play-26",
    "uk.gov.hmrc" %% "play-hmrc-api" % "4.1.0-play-26"
  )
  val test = Seq(
    "org.scalatest" %% "scalatest" % "3.0.5",
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2",
    "org.pegdown" % "pegdown" % "1.6.0",
    "org.mockito" % "mockito-core" % "2.23.0",
    "com.github.tomakehurst" % "wiremock-jre8" % "2.21.0",
    "com.typesafe.play" %% "play-test" % PlayVersion.current
  )
}