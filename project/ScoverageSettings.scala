import sbt.Keys.parallelExecution
import sbt.{Def, *}
import scoverage.ScoverageKeys

object ScoverageSettings {
  def apply(): Seq[Def.Setting[_ >: String with Double with Boolean]] = Seq( // Semicolon-separated list of regexes matching classes to exclude
    ScoverageKeys.coverageExcludedPackages := "<empty>;Reverse.*;.*(config|views.*);.*(AuthService|BuildInfo|Routes).*",
    ScoverageKeys.coverageExcludedFiles := Seq(
      "" +
        "<empty>",
      "Reverse.*",
      ".*models.*",
      ".*repositories.*",
      ".*BuildInfo.*",
      ".*javascript.*",
      ".*Routes.*",
      ".*GuiceInjector",
      ".*DateTimeQueryStringBinder.*", // better covered via wiremock/E2E integration tests
      ".*Test.*"
    ).mkString(";"),
    ScoverageKeys.coverageMinimumStmtTotal := 70,  //should be a lot higher but we are where we are
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true,
    parallelExecution in Test := false
  )
}
