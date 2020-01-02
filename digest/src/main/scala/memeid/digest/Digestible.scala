package memeid.digest

import java.nio.charset.StandardCharsets.UTF_8

trait Digestible[A] { self =>

  //TODO: Remove this and add ContravariantFunctor[Digestible] in `memeid-cats`
  def contramap[B](f: B => A): Digestible[B] = { a: B =>
    self.toByteArray(f(a))
  }

  def toByteArray(a: A): Array[Byte]

}

object Digestible {

  def apply[A](implicit d: Digestible[A]): Digestible[A] = d

  implicit val DigestibleStringImplementation: Digestible[String] = _.getBytes(UTF_8)

}
