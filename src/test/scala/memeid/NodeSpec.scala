package memeid.node

import cats.effect._

import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class NodeSpec extends Specification with ScalaCheck {
  "Node.clockSequence" should {
    "be initialized once per system lifetime" in {
      val node1 = Node[IO]
      val node2 = Node[IO]
      val node3 = Node[IO]

      val clockSeq = node1.clockSequence.unsafeRunSync
      node2.clockSequence.unsafeRunSync must be equalTo (clockSeq)
      node3.clockSequence.unsafeRunSync must be equalTo (clockSeq)
    }
  }

  "Node.nodeId" should {
    "be equal for the same node" in {
      val node1  = Node[IO]
      val nodeId = node1.nodeId.unsafeRunSync

      val node2 = Node[IO]
      val node3 = Node[IO]

      node2.nodeId.unsafeRunSync must be equalTo (nodeId)
      node3.nodeId.unsafeRunSync must be equalTo (nodeId)
    }
  }
}
