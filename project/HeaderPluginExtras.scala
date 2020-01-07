import java.time.LocalDate.now

import de.heikoseeberger.sbtheader.HeaderPlugin
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.HeaderLicense.ALv2
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport._
import sbt._

object HeaderPluginExtras extends AutoPlugin {

  //TODO Remove once tasks are contributed to sbt-header
  object autoImport {

    val headerCreateAll = taskKey[Unit]("Create/update headers in all configurations")

    val headerCheckAll = taskKey[Unit]("Check whether files have headers in all configurations")

  }

  import autoImport._

  override def trigger = allRequirements

  override def requires: Plugins = HeaderPlugin

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    headerLicense   := Some(ALv2(s"2019-${now.getYear}", "47 Degrees, LLC. <http://www.47deg.com>")),
    headerCreateAll := headerCreate.?.all(ScopeFilter(configurations = inAnyConfiguration)).value,
    headerCheckAll  := headerCheck.?.all(ScopeFilter(configurations = inAnyConfiguration)).value
  )

}
