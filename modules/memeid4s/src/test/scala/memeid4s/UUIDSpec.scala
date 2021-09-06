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

package memeid4s

import memeid4s.UUID.RichUUID
import memeid4s.scalacheck.arbitrary.instances._
import org.scalacheck.Gen
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

@SuppressWarnings(Array("scalafix:Disable.toString"))
class UUIDSpec extends Specification with ScalaCheck {

  "UUID.from" should {

    "return Right on valid UUID" in prop { uuid: UUID =>
      UUID.from(uuid.toString) should beRight(uuid)
    }

    "return Left on invalid string" in prop { s: String =>
      UUID.from(s) must beLeft().like { case e: IllegalArgumentException =>
        e.getMessage must contain("UUID string")
      }
    }.setGen(Gen.alphaNumStr)

  }

  "UUID.as" should {

    "return Some[UUID.V1] only if version is 1" in prop { uuid: UUID.V1 =>
      (uuid.as[UUID.V1] must beSome(uuid))
        .and(uuid.as[UUID.V2] must beNone)
        .and(uuid.as[UUID.V3] must beNone)
        .and(uuid.as[UUID.V4] must beNone)
        .and(uuid.as[UUID.V5] must beNone)
    }

    "return Some[UUID.V2] only if version is 2" in prop { uuid: UUID.V2 =>
      (uuid.as[UUID.V1] must beNone)
        .and(uuid.as[UUID.V2] must beSome(uuid))
        .and(uuid.as[UUID.V3] must beNone)
        .and(uuid.as[UUID.V4] must beNone)
        .and(uuid.as[UUID.V5] must beNone)
    }

    "return Some[UUID.V3] only if version is 3" in prop { uuid: UUID.V3 =>
      (uuid.as[UUID.V1] must beNone)
        .and(uuid.as[UUID.V2] must beNone)
        .and(uuid.as[UUID.V3] must beSome(uuid))
        .and(uuid.as[UUID.V4] must beNone)
        .and(uuid.as[UUID.V5] must beNone)
    }

    "return Some[UUID.V4] only if version is 4" in prop { uuid: UUID.V4 =>
      (uuid.as[UUID.V1] must beNone)
        .and(uuid.as[UUID.V2] must beNone)
        .and(uuid.as[UUID.V3] must beNone)
        .and(uuid.as[UUID.V4] must beSome(uuid))
        .and(uuid.as[UUID.V5] must beNone)
    }

    "return Some[UUID.V5] only if version is 5" in prop { uuid: UUID.V5 =>
      (uuid.as[UUID.V1] must beNone)
        .and(uuid.as[UUID.V2] must beNone)
        .and(uuid.as[UUID.V3] must beNone)
        .and(uuid.as[UUID.V4] must beNone)
        .and(uuid.as[UUID.V5] must beSome(uuid))
    }

  }

  "UUID.is" should {

    "return true only if version is 1" in prop { uuid: UUID.V1 =>
      (uuid.is[UUID.V1] must beTrue)
        .and(uuid.is[UUID.V2] must beFalse)
        .and(uuid.is[UUID.V3] must beFalse)
        .and(uuid.is[UUID.V4] must beFalse)
        .and(uuid.is[UUID.V5] must beFalse)
    }

    "return true only if version is 2" in prop { uuid: UUID.V2 =>
      (uuid.is[UUID.V1] must beFalse)
        .and(uuid.is[UUID.V2] must beTrue)
        .and(uuid.is[UUID.V3] must beFalse)
        .and(uuid.is[UUID.V4] must beFalse)
        .and(uuid.is[UUID.V5] must beFalse)
    }

    "return true only if version is 3" in prop { uuid: UUID.V3 =>
      (uuid.is[UUID.V1] must beFalse)
        .and(uuid.is[UUID.V2] must beFalse)
        .and(uuid.is[UUID.V3] must beTrue)
        .and(uuid.is[UUID.V4] must beFalse)
        .and(uuid.is[UUID.V5] must beFalse)
    }

    "return true only if version is 4" in prop { uuid: UUID.V4 =>
      (uuid.is[UUID.V1] must beFalse)
        .and(uuid.is[UUID.V2] must beFalse)
        .and(uuid.is[UUID.V3] must beFalse)
        .and(uuid.is[UUID.V4] must beTrue)
        .and(uuid.is[UUID.V5] must beFalse)
    }

    "return true only if version is 5" in prop { uuid: UUID.V5 =>
      (uuid.is[UUID.V1] must beFalse)
        .and(uuid.is[UUID.V2] must beFalse)
        .and(uuid.is[UUID.V3] must beFalse)
        .and(uuid.is[UUID.V4] must beFalse)
        .and(uuid.is[UUID.V5] must beTrue)
    }

  }

  "uuid.msb" should {

    "be an alias for uuid.getMostSignificantBits()" in prop { uuid: UUID =>
      (uuid.msb must be).equalTo(uuid.getMostSignificantBits)
    }

  }

  "uuid.lsb" should {

    "be an alias for uuid.getLeastSignificantBits()" in prop { uuid: UUID =>
      (uuid.lsb must be).equalTo(uuid.getLeastSignificantBits)
    }

  }

  "UUID.variant" should {

    "detect a valid variant" in prop { msb: Long =>
      val uuid = UUID.from(msb, 0x9000000000000000L)

      (uuid.variant must be).equalTo(2)
    }

    "detect an invalid UUID variant" in prop { msb: Long =>
      val uuid = UUID.from(msb, 0xc000000000000000L)

      uuid.variant must not be equalTo(2)
    }

  }

  "UUID.Nil" should {

    "have variant 0" in {
      (UUID.Nil.variant must be).equalTo(0)
    }

    "have all 128 bits to 0" in {
      ((UUID.Nil.msb must be).equalTo(0L)).and((UUID.Nil.lsb must be).equalTo(0L))
    }

  }

  "unapply" should {

    "extract valid uuid string as UUID" in prop { uuid: UUID =>
      (UUID.unapply(uuid.toString) must be).some(uuid)
    }

    "fail on invalid uuid string as UUID" in prop { string: String =>
      UUID.unapply(string) must beNone
    }.setGen(Gen.alphaNumStr)

  }

}
