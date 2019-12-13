package memeid

import java.util.{UUID => JUUID}

import memeid.JavaConverters._

trait Constructors {

  /**
   * Creates a valid [[UUID]] from two [[Long]] values representing
   * the most/least significant bits.
   */
  def from(msb: Long, lsb: Long): UUID = new JUUID(msb, lsb).asScala

}
