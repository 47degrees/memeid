package memeid.circe

import java.util.{UUID => JUUID}

import io.circe.{Decoder, Encoder}
import memeid.JavaConverters._
import memeid.UUID

object implicits {

  implicit def memeidEncoder(implicit E: Encoder[JUUID]): Encoder[UUID] =
    E.contramap(_.asJava)

  implicit def memeidDecoder(implicit D: Decoder[JUUID]): Decoder[UUID] =
    D.map(_.asScala)
}
