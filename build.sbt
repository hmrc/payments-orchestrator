import uk.gov.hmrc.DefaultBuildSettings.{defaultSettings, integrationTestSettings, scalaSettings}
import uk.gov.hmrc.SbtArtifactory
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings
import wartremover.{Wart, wartremoverErrors, wartremoverExcluded}

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory)
  .settings(
    scalaVersion := "2.11.12",
    resolvers ++= Seq(Resolver.bintrayRepo("hmrc", "releases"), Resolver.jcenterRepo),
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    retrieveManaged := false,
    routesGenerator := InjectedRoutesGenerator,
    evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false)
  )
  .settings(majorVersion := 0)
  .settings(ScalariformSettings())
  .settings(WartRemoverSettings.wartRemoverError)
  .settings(WartRemoverSettings.wartRemoverWarning)
  .settings(wartremoverErrors in(Test, compile) --= Seq(Wart.Any, Wart.Equals, Wart.Null, Wart.NonUnitStatements, Wart.PublicInference))
  .settings(wartremoverExcluded ++=
    routes.in(Compile).value ++
      (baseDirectory.value / "it").get ++
      (baseDirectory.value / "test").get ++
      Seq(sourceManaged.value / "main" / "sbt-buildinfo" / "BuildInfo.scala"))
  .settings(ScoverageSettings())
  .settings(publishingSettings: _*)
  .settings(PlayKeys.playDefaultPort := 8418)
  .settings(scalaSettings: _*)
  .settings(defaultSettings(): _*)
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
      "-Yno-adapted-args",
      "-Ywarn-value-discard",
      "-Ywarn-dead-code",
      "-deprecation",
      "-feature",
      "-unchecked",
      "-language:implicitConversions",
      "-language:reflectiveCalls",
      "-Ypartial-unification" //required by cats
    )
  )


val appName = "payments-orchestrator"