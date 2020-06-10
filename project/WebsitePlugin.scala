import mdoc.MdocPlugin
import microsites.MicrositesPlugin
import microsites.MicrositesPlugin.autoImport._
import sbt._

object WebsitePlugin extends AutoPlugin {
  override def requires: Plugins = MdocPlugin && MicrositesPlugin

  override def trigger: PluginTrigger = allRequirements

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      micrositeName                := "memeid",
      micrositeBaseUrl             := "memeid",
      micrositeDocumentationUrl    := "docs",
      micrositeGithubToken         := Option(System.getenv().get("GITHUB_TOKEN")),
      micrositePushSiteWith        := GitHub4s,
      micrositeHighlightLanguages ++= Seq("java", "scala", "kotlin", "groovy")
    )
}
