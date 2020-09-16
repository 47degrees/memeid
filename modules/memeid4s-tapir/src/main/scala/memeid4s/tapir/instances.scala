package memeid4s.tapir

import memeid4s.UUID
import sttp.tapir.Codec
import sttp.tapir.CodecFormat
import sttp.tapir.DecodeResult
import sttp.tapir.Schema
import sttp.tapir.SchemaType.SString

@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract"))
trait instances {

  /**
   * Allows reading `UUID` values from path, query params, or headers
   */
  implicit val UUIDCodec: Codec[String, UUID, CodecFormat.TextPlain] =
    Codec.string
      .mapDecode(s => UUID.from(s).fold(DecodeResult.Error(s, _), DecodeResult.Value(_)))(_.toString) // scalafix:ok
      .modifySchema(_.format("uuid"))

  /**
   * Provides a valid `Schema` for `UUID` type, to be used when generating documentation.
   */
  implicit val UUIDSchema: Schema[UUID] = Schema(SString).format("uuid")

}

object instances extends instances
