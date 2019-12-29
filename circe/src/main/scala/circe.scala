package memeid.circe

import scala.util.Try

import memeid.UUID
import memeid.JavaConverters._

import java.util.{ UUID => JUUID }

import cats.syntax.show._

import io.circe.{ Encoder, Decoder }


object implicits {
  implicit val memeidEncoder: Encoder[UUID] =
    Encoder.encodeString.contramap(_.show)

  implicit val memeidDecoder: Decoder[UUID] =
    Decoder.decodeString.emapTry(str => {
      Try(JUUID.fromString(str)).map(_.asScala)
    })
}
