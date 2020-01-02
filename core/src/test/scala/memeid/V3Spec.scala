package memeid

import org.specs2.ScalaCheck
import org.specs2.matcher.IOMatchers
import org.specs2.mutable.Specification

class V3Spec extends Specification with ScalaCheck with IOMatchers {

  "V3 constructor" should {

    "create same UUID for the same namespace/name" in {
      val namespace = UUID.v4
      val name      = "a-thing"

      val uuid1 = UUID.v3(namespace, name)
      val uuid2 = UUID.v3(namespace, name)

      uuid1 must be equalTo uuid2
    }

    "create different UUIDs for distinct name" in {
      val namespace = UUID.v4
      val name1     = "a-thing"
      val name2     = "b-thing"

      val uuid1 = UUID.v3(namespace, name1)
      val uuid2 = UUID.v3(namespace, name2)

      uuid1 must not be equalTo(uuid2)
    }

    "create different UUIDs for distinct namespace" in {
      val namespace1 = UUID.v4
      val namespace2 = UUID.v4
      val name       = "a-thing"

      val uuid1 = UUID.v3(namespace1, name)
      val uuid2 = UUID.v3(namespace2, name)

      uuid1 must not be equalTo(uuid2)
    }

  }

}
