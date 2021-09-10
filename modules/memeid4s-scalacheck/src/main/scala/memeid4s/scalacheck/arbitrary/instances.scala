/*
 * Copyright 2019-2021 47 Degrees Open Source <https://www.47deg.com>
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

package memeid4s.scalacheck.arbitrary

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

import memeid.Node
import memeid.UUID
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary

@SuppressWarnings(
  Array("scalafix:DisableSyntax.isInstanceOf", "scalafix:DisableSyntax.asInstanceOf")
)
object instances {

  implicit val UUIDArbitraryInstance: Arbitrary[UUID] = Arbitrary {
    for {
      msb <- arbitrary[Long]
      lsb <- arbitrary[Long]
    } yield UUID.from(msb, lsb)
  }

  implicit val UUIDV1ArbitraryInstance: Arbitrary[UUID.V1] = Arbitrary {
    for {
      timestamp     <- arbitrary[Long]
      clockSequence <- arbitrary[Short]
      id            <- arbitrary[Long]
      node           = new Node(clockSequence, id)
    } yield UUID.V1.next(node, () => timestamp).asInstanceOf[UUID.V1]
  }

  implicit val UUIDV2ArbitraryInstance: Arbitrary[UUID.V2] = Arbitrary {
    arbitrary[UUID].retryUntil(_.isInstanceOf[UUID.V2]).map(_.asInstanceOf[UUID.V2])
  }

  implicit val UUIDV3ArbitraryInstance: Arbitrary[UUID.V3] = Arbitrary {
    for {
      namespace <- arbitrary[UUID]
      name      <- arbitrary[String]
    } yield UUID.V3.from(namespace, name).asInstanceOf[UUID.V3]
  }

  implicit val UUIDV4ArbitraryInstance: Arbitrary[UUID.V4] = Arbitrary {
    for {
      msb <- arbitrary[Long]
      lsb <- arbitrary[Long]
    } yield UUID.V4.from(msb, lsb).asInstanceOf[UUID.V4]
  }

  implicit val UUIDV5ArbitraryInstance: Arbitrary[UUID.V5] = Arbitrary {
    for {
      namespace <- arbitrary[UUID]
      name      <- arbitrary[String]
    } yield UUID.V5.from(namespace, name).asInstanceOf[UUID.V5]
  }

}
