import java.time.LocalDate.now

import de.heikoseeberger.sbtheader.HeaderPlugin
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.HeaderLicense.ALv2
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport._
import sbt._

object HeaderPluginSettings extends AutoPlugin {

  override def trigger = allRequirements

  override def requires: Plugins = HeaderPlugin

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    headerLicense := Some(ALv2(s"2019-${now.getYear}", "47 Degrees, LLC. <http://www.47deg.com>"))
  )

}
