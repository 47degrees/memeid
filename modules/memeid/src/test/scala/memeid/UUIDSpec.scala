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

package memeid

import java.util.Optional

import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

@SuppressWarnings(Array("scalafix:Disable.toString", "scalafix:DisableSyntax.asInstanceOf"))
class UUIDSpec extends Specification with ScalaCheck {

  "UUID.fromString" should {

    "return uuid on valid uuid string" in prop { uuid: UUID =>
      UUID.fromString(uuid.toString) should be equalTo uuid
    }

    "throw exception on an invalid string that is accepted by java.util.fromString" >> {
      UUID.fromString("1-1-1-1-1") must throwAn[IllegalArgumentException]
    }

    "throw exception on invalid string" in prop { s: String =>
      UUID.fromString(s) must throwAn[IllegalArgumentException]
    }.setGen(Gen.alphaNumStr)

  }

  "UUID.fromUUID" should {

    "return NIL on NIL UUID" >> {
      UUID.fromUUID(UUID.NIL.asJava) must be equalTo UUID.NIL
    }

    "return uuid depending on version" in prop { uuid: UUID =>
      UUID.fromUUID(uuid.asJava) must be like {
        case _: UUID.V1             => uuid.version must be equalTo 1
        case _: UUID.V2             => uuid.version must be equalTo 2
        case _: UUID.V3             => uuid.version must be equalTo 3
        case _: UUID.V4             => uuid.version must be equalTo 4
        case _: UUID.V5             => uuid.version must be equalTo 5
        case _: UUID.UnknownVersion => uuid.version must not be between(1, 5)
      }
    }

  }

  "uuid.asV* methods" should {

    "uuid.asV1 should return optional with uuid only if class is UUID.V1" >> {
      val uuid = UUID.V1.next.asInstanceOf[UUID.V1]

      (uuid.asV1 must be equalTo Optional.of[UUID.V1](uuid)) and
        (uuid.asV2 must be equalTo Optional.empty[UUID.V2]) and
        (uuid.asV3 must be equalTo Optional.empty[UUID.V3]) and
        (uuid.asV4 must be equalTo Optional.empty[UUID.V4]) and
        (uuid.asV5 must be equalTo Optional.empty[UUID.V5])
    }

    "uuid.asV2 should return optional with uuid only if class is UUID.V2" in prop { uuid: UUID.V2 =>
      (uuid.asV1 must be equalTo Optional.empty[UUID.V1]) and
        (uuid.asV2 must be equalTo Optional.of[UUID.V2](uuid)) and
        (uuid.asV3 must be equalTo Optional.empty[UUID.V3]) and
        (uuid.asV4 must be equalTo Optional.empty[UUID.V4]) and
        (uuid.asV5 must be equalTo Optional.empty[UUID.V5])
    }

    "uuid.asV3 should return optional with uuid only if class is UUID.V3" >> {
      val uuid = UUID.V3.from(UUID.V4.random, "miau").asInstanceOf[UUID.V3]

      (uuid.asV1 must be equalTo Optional.empty[UUID.V1]) and
        (uuid.asV2 must be equalTo Optional.empty[UUID.V2]) and
        (uuid.asV3 must be equalTo Optional.of[UUID.V3](uuid)) and
        (uuid.asV4 must be equalTo Optional.empty[UUID.V4]) and
        (uuid.asV5 must be equalTo Optional.empty[UUID.V5])
    }

    "uuid.asV4 should return optional with uuid only if class is UUID.V4" >> {
      val uuid = UUID.V4.random.asInstanceOf[UUID.V4]

      (uuid.asV1 must be equalTo Optional.empty[UUID.V1]) and
        (uuid.asV2 must be equalTo Optional.empty[UUID.V2]) and
        (uuid.asV3 must be equalTo Optional.empty[UUID.V3]) and
        (uuid.asV4 must be equalTo Optional.of[UUID.V4](uuid)) and
        (uuid.asV5 must be equalTo Optional.empty[UUID.V5])
    }

    "uuid.asV5 should return optional with uuid only if class is UUID.V5" >> {
      val uuid = UUID.V5.from(UUID.V4.random, "miau").asInstanceOf[UUID.V5]

      (uuid.asV1 must be equalTo Optional.empty[UUID.V1]) and
        (uuid.asV2 must be equalTo Optional.empty[UUID.V2]) and
        (uuid.asV3 must be equalTo Optional.empty[UUID.V3]) and
        (uuid.asV4 must be equalTo Optional.empty[UUID.V4]) and
        (uuid.asV5 must be equalTo Optional.of[UUID.V5](uuid))
    }

  }

  "uuid.isV* methods" should {

    "uuid.isV1 should return true only if class is UUID.V1" >> {
      val uuid = UUID.V1.next

      (uuid.isNil must beFalse) and
        (uuid.isV1 must beTrue) and
        (uuid.isV2 must beFalse) and
        (uuid.isV3 must beFalse) and
        (uuid.isV4 must beFalse) and
        (uuid.isV5 must beFalse)
    }

    "uuid.isV2 should return true only if class is UUID.V2" in prop { uuid: UUID.V2 =>
      (uuid.isNil must beFalse) and
        (uuid.isV1 must beFalse) and
        (uuid.isV2 must beTrue) and
        (uuid.isV3 must beFalse) and
        (uuid.isV4 must beFalse) and
        (uuid.isV5 must beFalse)
    }

    "uuid.isV3 should return true only if class is UUID.V3" >> {
      val uuid = UUID.V3.from(UUID.V4.random, "miau")

      (uuid.isNil must beFalse) and
        (uuid.isV1 must beFalse) and
        (uuid.isV2 must beFalse) and
        (uuid.isV3 must beTrue) and
        (uuid.isV4 must beFalse) and
        (uuid.isV5 must beFalse)
    }

    "uuid.isV4 should return true only if class is UUID.V4" >> {
      val uuid = UUID.V4.random

      (uuid.isNil must beFalse) and
        (uuid.isV1 must beFalse) and
        (uuid.isV2 must beFalse) and
        (uuid.isV3 must beFalse) and
        (uuid.isV4 must beTrue) and
        (uuid.isV5 must beFalse)
    }

    "uuid.isV5 should return true only if class is UUID.V5" >> {
      val uuid = UUID.V5.from(UUID.V4.random, "miau")

      (uuid.isNil must beFalse) and
        (uuid.isV1 must beFalse) and
        (uuid.isV2 must beFalse) and
        (uuid.isV3 must beFalse) and
        (uuid.isV4 must beFalse) and
        (uuid.isV5 must beTrue)
    }

    "uuid.isNil should return true only if uuid is NIL uuid" >> {
      (UUID.NIL.isNil must beTrue) and
        (UUID.NIL.isV1 must beFalse) and
        (UUID.NIL.isV2 must beFalse) and
        (UUID.NIL.isV3 must beFalse) and
        (UUID.NIL.isV4 must beFalse) and
        (UUID.NIL.isV5 must beFalse)
    }

  }

  "UUID.variant" should {

    "detect a valid variant" in prop { msb: Long =>
      val uuid = UUID.from(msb, 0x9000000000000000L)

      uuid.variant must be equalTo 2
    }

    "detect an invalid UUID variant" in prop { msb: Long =>
      val uuid = UUID.from(msb, 0xc000000000000000L)

      uuid.variant must not be equalTo(2)
    }

  }

  "UUID.NIL" should {

    "have variant 0" in {
      UUID.NIL.variant must be equalTo 0
    }

    "have all 128 bits to 0" in {
      (UUID.NIL.getMostSignificantBits must be equalTo 0L) and
        (UUID.NIL.getLeastSignificantBits must be equalTo 0L)
    }

  }

  implicit val UUIDArbitraryInstance: Arbitrary[UUID] = Arbitrary {
    for {
      msb <- arbitrary[Long]
      lsb <- arbitrary[Long]
    } yield UUID.from(msb, lsb)
  }

  @SuppressWarnings(Array("scalafix:DisableSyntax.isInstanceOf"))
  implicit val UUIDV2ArbitraryInstance: Arbitrary[UUID.V2] = Arbitrary {
    arbitrary[UUID].retryUntil(_.isInstanceOf[UUID.V2]).map(_.asInstanceOf[UUID.V2])
  }

}
