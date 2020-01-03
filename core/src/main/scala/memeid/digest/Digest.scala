package memeid.digest

object Digest {

  def hash(algo: Algorithm)(arrs: Seq[Array[Byte]]): Array[Byte] = {
    val digest = algo.digest
    arrs.foreach(b => digest.update(b))
    digest.digest
  }

  def md5(arrs: Seq[Array[Byte]]): Array[Byte] =
    hash(MD5)(arrs)
}
