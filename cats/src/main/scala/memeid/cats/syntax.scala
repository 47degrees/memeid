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

package memeid.cats

import java.util.concurrent.TimeUnit.SECONDS

import cats.effect.{Clock, Sync}
import cats.syntax.functor._

import memeid.UUID
import memeid.digest.Digestible
import memeid.node.Node
import memeid.time.{Posix, Time}

trait syntax {

  implicit class UUIDtoCatsConstructors(companion: UUID.type) {

    def v1[F[_]: Sync](implicit N: Node, T: Time): F[UUID] = F.delay(UUID.V1.next(N, T))

    def v3[F[_]: Sync, A: Digestible](namespace: UUID, local: A): F[UUID] =
      F.delay(UUID.V3(namespace, local))

    def v5[F[_]: Sync, A: Digestible](namespace: UUID, local: A): F[UUID] =
      F.delay(UUID.V5(namespace, local))

    def random[F[_]: Sync]: F[UUID] = F.delay(UUID.V4.random)

    def squuid[F[_]: Sync: Clock]: F[UUID] = F.realTime(SECONDS).map { s =>
      UUID.V4.squuid {
        new Posix {
          override def value: Long = s
        }
      }
    }

  }

}

object syntax extends syntax
