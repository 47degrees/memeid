/*
 * Copyright 2019-2022 47 Degrees Open Source <https://www.47deg.com>
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

package memeid4s.cats

import java.util.{UUID => JUUID}

import cats.instances.uuid._
import cats.kernel.Hash
import cats.kernel.Order
import cats.syntax.contravariant._
import cats.syntax.show._

import memeid4s.UUID
import memeid4s.cats.instances._
import memeid4s.digest.Digestible
import memeid4s.scalacheck.arbitrary.instances._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

@SuppressWarnings(Array("scalafix:Disable.toString"))
class InstancesSpec extends Specification with ScalaCheck {

  "Show[UUID]" should {

    "allow representing uuids as String" in prop { uuid: UUID =>
      uuid.show must be equalTo uuid.toString
    }

  }

  "Contravariant[Digestible]" should {

    "be provided" >> {
      val intDigestible: Digestible[Int] = Digestible[String].contramap(_.toString)

      val expected = Digestible[String].toByteArray(4.toString)

      intDigestible.toByteArray(4) must be equalTo expected
    }

  }

  "Order[UUID]" should {

    "compare using unsigned comparison" in prop { lsb: Long =>
      // specifically chosen since they will not compare correctly unless using unsigned comparison
      val uuid1 = UUID.from(0x20000000.toLong, lsb)
      val uuid2 = UUID.from(0xe0000000.toLong, lsb)

      (Order[UUID].compare(uuid1, uuid2) must be equalTo -1) and
        (Order[JUUID].compare(uuid1.asJava, uuid2.asJava) must be equalTo 1)
    }

  }

  "Hash[UUID] returns hash code consistent with java.util.UUID" in prop { (msb: Long, lsb: Long) =>
    val uuid = UUID.from(msb, lsb)

    Hash[UUID].hash(uuid) must be equalTo Hash[JUUID].hash(uuid.asJava)
  }

}
