package memeid

import org.specs2.ScalaCheck
import org.specs2.matcher.IOMatchers
import org.specs2.mutable.Specification

class V5Spec extends Specification with ScalaCheck with IOMatchers {

  "V5 constructor" should {

    "create same UUID for the same namespace/name" in {
      val namespace = UUID.V4.random
      val name      = "a-thing"

      val uuid1 = UUID.V3(namespace, name)
      val uuid2 = UUID.V3(namespace, name)

      uuid1 must be equalTo uuid2
    }

    "create different UUIDs for distinct name" in {
      val namespace = UUID.V4.random
      val name1     = "a-thing"
      val name2     = "b-thing"

      val uuid1 = UUID.V3(namespace, name1)
      val uuid2 = UUID.V3(namespace, name2)

      uuid1 must not be equalTo(uuid2)
    }

    "create different UUIDs for distinct namespace" in {
      val namespace1 = UUID.V4.random
      val namespace2 = UUID.V4.random
      val name       = "a-thing"

      val uuid1 = UUID.V3(namespace1, name)
      val uuid2 = UUID.V3(namespace2, name)

      uuid1 must not be equalTo(uuid2)
    }

  }

}
