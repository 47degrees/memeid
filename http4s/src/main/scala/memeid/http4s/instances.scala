package memeid.http4s

import memeid.cats.instances._
import cats.syntax.show._
import cats.syntax.either._
import memeid.UUID
import org.http4s.{ParseFailure, QueryParamDecoder, QueryParamEncoder}

trait instances {

  /** Allow reading UUIDs from a request's query params */
  implicit val UUIDQueryParamDecoderInstance: QueryParamDecoder[UUID] =
    QueryParamDecoder[String].emap { s =>
      UUID.from(s).leftMap { e =>
        ParseFailure(s"Failed to decode value: $s as UUID", e.getMessage)
      }
    }

  /** Allow using UUIDs in a request's query params */
  implicit val UUIDQueryParamEncoderInstance: QueryParamEncoder[UUID] =
    QueryParamEncoder[String].contramap(_.show)

}

object instances extends instances
