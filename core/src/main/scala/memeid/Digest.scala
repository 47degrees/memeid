package memeid.digest

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

sealed trait Algorithm {
  def digest: MessageDigest
}

case object MD5 extends Algorithm {
  def digest: MessageDigest = MessageDigest.getInstance("MD5")
}

case object SHA1 extends Algorithm {
  def digest: MessageDigest = MessageDigest.getInstance("SHA1")
}

trait Digestible[A] { self =>

  def contramap[B](f: B => A): Digestible[B] = {
    new Digestible[B] {
      def toByteArray(a: B): Array[Byte] =
        self.toByteArray(f(a))
    }
  }

  def toByteArray(a: A): Array[Byte]
}

object Digestible {
  def apply[A](implicit d: Digestible[A]): Digestible[A] = d

  implicit val digestibleString: Digestible[String] =
    _.getBytes(StandardCharsets.UTF_8)

}

object Digest {

  def hash(algo: Algorithm)(arrs: Seq[Array[Byte]]): Array[Byte] = {
    val digest = algo.digest
    arrs.foreach(b => digest.update(b))
    digest.digest
  }

  def md5(arrs: Seq[Array[Byte]]): Array[Byte] =
    hash(MD5)(arrs)
}
