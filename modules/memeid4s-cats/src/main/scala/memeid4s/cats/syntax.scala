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

package memeid4s.cats

import cats.effect.Clock
import cats.effect.Sync
import cats.syntax.functor._

import memeid4s.UUID
import memeid4s.digest.Digestible
import memeid4s.node.Node
import memeid4s.time.Posix
import memeid4s.time.Time

trait syntax {

  implicit class UUIDtoCatsConstructors(companion: UUID.type) {

    def v1[F[_]: Sync](implicit N: Node, T: Time): F[UUID] = Sync[F].delay(UUID.V1.next(N, T))

    def v3[F[_]: Sync, A: Digestible](namespace: UUID, local: A): F[UUID] =
      Sync[F].delay(UUID.V3(namespace, local))

    def v5[F[_]: Sync, A: Digestible](namespace: UUID, local: A): F[UUID] =
      Sync[F].delay(UUID.V5(namespace, local))

    def random[F[_]: Sync]: F[UUID] = Sync[F].delay(UUID.V4.random)

    def squuid[F[_]: Sync]: F[UUID] =
      Clock[F].realTime.map { d =>
        UUID.V4.squuid {
          new Posix {
            override def value: Long = d.toSeconds
          }
        }
      }

  }

}

object syntax extends syntax
