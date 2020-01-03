package memeid.cats

import cats.effect.Sync

import memeid.UUID
import memeid.digest.Digestible
import memeid.node.Node
import memeid.time.Time

trait syntax {

  implicit class UUIDtoCatsConstructors(companion: UUID.type) {

    def v1[F[_]: Sync](implicit N: Node, T: Time): F[UUID] = Sync[F].delay(UUID.V1.next(N, T))

    def v3[F[_]: Sync, A: Digestible](namespace: UUID, local: A): F[UUID] =
      Sync[F].delay(UUID.V3(namespace, local))

    def v5[F[_]: Sync, A: Digestible](namespace: UUID, local: A): F[UUID] =
      Sync[F].delay(UUID.V5(namespace, local))

    def random[F[_]: Sync]: F[UUID] = Sync[F].delay(UUID.V4.random)

    def squuid[F[_]: Sync](implicit T: Time): F[UUID] = Sync[F].delay(UUID.V4.squuid(T))

  }

}

object syntax extends syntax
