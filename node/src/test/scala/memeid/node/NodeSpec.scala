package memeid.node

import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class NodeSpec extends Specification with ScalaCheck {

  "Node.clockSequence" should {
    "be initialized once per system lifetime" in {
      val node1 = implicitly[Node]
      val node2 = implicitly[Node]
      val node3 = implicitly[Node]

      val clockSeq = node1.clockSequence

      node2.clockSequence must be equalTo clockSeq
      node3.clockSequence must be equalTo clockSeq
    }
  }

  "Node.nodeId" should {
    "be equal for the same node" in {
      val node1 = implicitly[Node]
      val node2 = implicitly[Node]
      val node3 = implicitly[Node]

      val nodeId = node1.nodeId

      node2.nodeId must be equalTo nodeId
      node3.nodeId must be equalTo nodeId
    }
  }

}
