package memeid.doobie

import java.util.{UUID => JUUID}

import doobie.util.{Get, Put}
import memeid.JavaConverters._
import memeid.UUID

object implicits {

  implicit def memeidGet(implicit G: Get[JUUID]): Get[UUID] =
    G.tmap(_.asScala)

  implicit def memeidPut(implicit G: Put[JUUID]): Put[UUID] =
    G.tcontramap(_.asJava)
}
