package memeid.node

import java.net.{InetAddress, NetworkInterface}

import scala.collection.JavaConverters._

@SuppressWarnings(Array("scalafix:Disable.toString"))
private[node] object Sys {

  def getNetworkInterfaces: Set[String] = {
    val localHost     = InetAddress.getLocalHost
    val hostName      = localHost.getCanonicalHostName
    val baseAddresses = Set(localHost.toString, hostName)
    NetworkInterface.getNetworkInterfaces.asScala.foldLeft(baseAddresses)({
      case (addrs, ni) =>
        addrs ++ ni.getInetAddresses.asScala.map(_.toString).toSet
    })
  }

  def getLocalInterfaces: Set[String] = {
    val localHost = InetAddress.getLocalHost
    val hostName  = localHost.getCanonicalHostName
    InetAddress.getAllByName(hostName).map(_.toString).toSet
  }

  def getProperties: Map[String, String] = {
    val props = System.getProperties
    val keys  = props.stringPropertyNames
    keys.asScala.map(k => k -> props.getProperty(k)).toMap
  }
}
