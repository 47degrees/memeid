import com.alejandrohdezma.sbt.github.SbtGithubPlugin
import sbt._
import sbt.Keys._

object descriptions extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = SbtGithubPlugin

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(description := descriptions.getOrElse(projectID.value.name, description.value))

  val descriptions = Map(
    "memeid4s"            -> "Scala wrapper for memeid, providing idiomatic constructors and simplifying library usage from Scala",
    "memeid4s-cats"       -> "Common type-classes instances and effectful constructors on top of cats for memeid4s",
    "memeid4s-literal"    -> "Macro interpolator to create UUID from literal strings in compile time",
    "memeid4s-doobie"     -> "Doobie type-classes instances to enable using memeid's UUID in doobie queries",
    "memeid4s-circe"      -> "Circe type-classes instances to enable decoding/encoding memeid's UUID values in JSON",
    "memeid4s-http4s"     -> "Http4s type-classes instances to enable using memeid's UUID as a query param in http4s services",
    "memeid4s-scalacheck" -> "Arbitrary instances for memeid's UUID as well as the different UUID versions",
    "memeid-kotlin"       -> "Kotlin wrapper for memeid, providing idiomatic constructors"
  )

}
