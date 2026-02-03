import uk.gov.hmrc.DefaultBuildSettings.{defaultSettings, scalaSettings}
import wartremover.WartRemover.autoImport.{wartremoverErrors, wartremoverExcluded}


scalaVersion := "3.5.2"

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .settings(
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    libraryDependencySchemes ++= Seq("org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always),
    retrieveManaged := false,
    routesGenerator := InjectedRoutesGenerator
//    update / evictionWarningOptions := EvictionWarningOptions.default.withWarnScalaVersionEviction(warnScalaVersionEviction = false)
  )
  .settings(majorVersion := 0)
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
  .settings(
    routesImport ++= Seq(
      "model.*",
      "model.des.*"
    ))
  .settings(
    scalacOptions ++= Seq(
      "-explain",
      "-Xfatal-warnings",
//      "-Xlint:-missing-interpolator,_",
      "-Wvalue-discard",
      "-deprecation",
      "-feature",
      "-unchecked",
      "-language:implicitConversions",
      "-language:reflectiveCalls",
      "-Wunused:imports",
      "-Wunused:privates",
      "-Wunused:locals",
      "-Wunused:explicits",
      "-Wunused:implicits",
      "-Wconf:src=routes/.*:s"
    )
  )
  .disablePlugins(JUnitXmlReportPlugin)


val appName = "payments-orchestrator"