import mdoc.MdocPlugin
import microsites.MicrositesPlugin
import microsites.MicrositesPlugin.autoImport._
import com.typesafe.sbt.site.SitePlugin.autoImport._
import sbt.Keys._
import sbt._

object WebsitePlugin extends AutoPlugin{
  override def requires: Plugins = MdocPlugin && MicrositesPlugin

  override def trigger: PluginTrigger = allRequirements

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    micrositeName                 := "memeid",
    micrositeBaseUrl              := "memeid",
    micrositeDocumentationUrl     := "docs",
    micrositeGithubOwner          := "47degrees",
    micrositeGithubRepo           := "memeid",
    micrositePushSiteWith         := GitHub4s,
    micrositeHighlightLanguages   ++= Seq("java", "scala"),
    includeFilter in makeSite     := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md" | "*.svg"
  )
}
