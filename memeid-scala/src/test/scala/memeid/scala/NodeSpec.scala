/*
 * Copyright 2019-2020 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package memeid.scala

import memeid.Node
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

      val id = node1.id

      node2.id must be equalTo id
      node3.id must be equalTo id
    }
  }

}
