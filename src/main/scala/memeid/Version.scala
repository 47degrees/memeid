package memeid

sealed trait Version

object Version {

  case object Null extends Version

  case object V1 extends Version

  case object V2 extends Version

  case object V3 extends Version

  case object V4 extends Version

  case object V5 extends Version

  final case class UnknownVersion(v: Int) extends Version

}
