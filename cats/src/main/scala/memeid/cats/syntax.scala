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

    def v1[F[_]: Sync](implicit N: Node, T: Time): F[UUID] = Sync[F].delay(UUID.V1.next(N, T))

    def v3[F[_]: Sync, A: Digestible](namespace: UUID, local: A): F[UUID] =
      Sync[F].delay(UUID.V3(namespace, local))

    def v5[F[_]: Sync, A: Digestible](namespace: UUID, local: A): F[UUID] =
      Sync[F].delay(UUID.V5(namespace, local))

    def random[F[_]: Sync]: F[UUID] = Sync[F].delay(UUID.V4.random)

    def squuid[F[_]: Sync: Clock]: F[UUID] =
      Clock[F]
        .realTime(SECONDS)
        .map { s =>
          UUID.V4.squuid {
            new Posix {
              override def value: Long = s
            }
          }
        }

  }

}

object syntax extends syntax
