import uk.gov.hmrc.DefaultBuildSettings.{defaultSettings, integrationTestSettings, scalaSettings}
import wartremover.WartRemover.autoImport.{wartremoverErrors, wartremoverExcluded}


val scalaV = "2.13.12"
scalaVersion := scalaV

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin)
  .settings(
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    retrieveManaged := false,
    routesGenerator := InjectedRoutesGenerator,
    update / evictionWarningOptions := EvictionWarningOptions.default.withWarnScalaVersionEviction(warnScalaVersionEviction = false)
  )
  .settings(majorVersion := 0)
  .settings(ScalariformSettings())
  .settings(WartRemoverSettings.wartRemoverError)
  .settings(WartRemoverSettings.wartRemoverWarning)
  .settings(Test / compile / wartremoverErrors --= Seq(Wart.Any, Wart.Equals, Wart.Null, Wart.NonUnitStatements, Wart.PublicInference))
  .settings(Compile / scalacOptions -= "utf8")
  .settings(wartremoverExcluded ++=
    (Compile / routes).value ++
      (baseDirectory.value / "it").get ++
      (baseDirectory.value / "test").get ++
      Seq(sourceManaged.value / "main" / "sbt-buildinfo" / "BuildInfo.scala"))
  .settings(ScoverageSettings())
  .settings(PlayKeys.playDefaultPort := 8418)
  .settings(scalaSettings *)
  .settings(defaultSettings() *)
  .settings(integrationTestSettings())
  .configs(IntegrationTest)
  .settings(
    routesImport ++= Seq(
      "model._",
      "model.des._"
    ))
  .settings(
    scalacOptions ++= Seq(
      "-Xfatal-warnings",
      "-Xlint:-missing-interpolator,_",
      "-Ywarn-value-discard",
      "-Ywarn-dead-code",
      "-deprecation",
      "-feature",
      "-unchecked",
      "-language:implicitConversions",
      "-language:reflectiveCalls",
      "-Ywarn-unused:-imports,-patvars,-privates,-locals,-explicits,-implicits,_"
    )
  )
  .disablePlugins(JUnitXmlReportPlugin)


val appName = "payments-orchestrator"