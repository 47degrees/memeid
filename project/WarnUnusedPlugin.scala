import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin

/**
 * Enables `-Ywarn-unused` in all projects.
 * This option is now required for 2.13 scalafix's RemoveUnused rule to work.
 */
object WarnUnusedPlugin extends AutoPlugin {

  override def requires: Plugins = JvmPlugin

  override def trigger: PluginTrigger = allRequirements

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    scalacOptions += "-Ywarn-unused"
  )

}
