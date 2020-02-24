import sbt.Keys.{pomPostProcess, publishArtifact, sLog}
import sbt.plugins.JvmPlugin
import sbt.{AutoPlugin, Def, Plugins, Test}

import scala.xml.transform.{RewriteRule, RuleTransformer}
import scala.xml.{Elem, Node, NodeSeq}

/**
 * This plugin automatically removes test dependencies from POMs for projects that
 * have `publishArtifact in Test` set to `false`.
 */
object RemoveTestFromPomPlugin extends AutoPlugin {

  override def requires: Plugins = JvmPlugin

  override def trigger = allRequirements

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    pomPostProcess := transformNode {
      case TestDependency(dependency) if !publishArtifact.in(Test).value =>
        sLog.value.warn(s"Test dependency $dependency has been omitted by $label")
        EmptyNodeSeq
    }
  )

  object TestDependency {

    def unapply(arg: Node): Option[String] = arg match {
      case elem: Elem =>
        Option(elem)
          .filter(_.label == "dependency")
          .filter(_.child.exists(child => child.label == "scope" && child.text == "test"))
          .map { e =>
            val organization = e.child.find(_.label == "groupId").map(_.text).mkString
            val artifact     = e.child.find(_.label == "artifactId").map(_.text).mkString
            val version      = e.child.find(_.label == "version").map(_.text).mkString

            s"$organization:$artifact:$version"
          }
      case _ => None
    }

  }

  def transformNode(p: PartialFunction[Node, NodeSeq]): Node => Node = { node =>
    val rule = new RewriteRule {
      override def transform(n: Node): Seq[Node] =
        if (p.isDefinedAt(n)) p(n) else n
    }

    val transformer = new RuleTransformer(rule)

    transformer.transform(node).head
  }

  object EmptyNodeSeq extends NodeSeq {
    override def theSeq: Seq[Node] = Seq()
  }

}
