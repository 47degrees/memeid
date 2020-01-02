package memeid.digest

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
