import java.io.{BufferedInputStream, File, FileInputStream}
import java.util.zip.GZIPInputStream

import DownloadActor.DownloadCSV
import akka.actor.{Actor, ActorRef, Props}

import scala.io.Source

object DownloadActor {
  def props(url: String): Props = Props(new DownloadActor(url))
  //#downloader-messages
  final case object DownloadCSV
}

class DownloadActor(url: String) extends Actor{
  import sys.process._
  import java.net.URL
  import java.io.File

  def receive = {
    case DownloadCSV => {
      val url = Config().getString("swrve_challenge.url")
      val filename = Config().getString("swrve_challenge.filename")
      val sizeBatches = Conversion.toInt(Config().getString("swrve_challenge.sizeBatches")).get
      new URL(url) #> new File(filename) !!

      val src = Source.fromInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(filename))))
      val lines = src.getLines()
      val batches = lines.sliding(sizeBatches,sizeBatches).toList
      sender() ! batches
    }
  }
}
