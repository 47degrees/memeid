import com.fortysevendeg.hood.plugin.SbtHoodPlugin
import com.fortysevendeg.hood.plugin.SbtHoodPlugin._
import sbt._

object HoodPlugin extends AutoPlugin {

  override def requires: Plugins = SbtHoodPlugin

  override def trigger = allRequirements

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      previousBenchmarkPath := file("bench/master.avgtime.csv"),
      currentBenchmarkPath  := file("bench/current.avgtime.csv"),
      token                 := Option(System.getenv().get("GITHUB_TOKEN")),
      repositoryOwner       := Option("47degrees"),
      repositoryName        := Option("memeid"),
      pullRequestNumber     := Option(System.getenv().get("PULL_REQUEST_NUMBER")).map(_.toInt),
      outputToFile          := true,
      outputPath            := file("bench/comparison.md"),
      benchmarkFiles        := List(file("bench/current.avgtime.csv")),
      uploadDirectory       := "bench",
      commitMessage         := "Update benchmark file"
    )
}
