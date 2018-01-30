import java.text.SimpleDateFormat
import java.util.Date

object Conversion {

  val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

  def toInt(s: String): Option[Int] = this.synchronized {
    try {
      Some(s.toInt)
    } catch {
      case e: Exception => None
    }
  }

  def toDate(s: String): Option[Date] = this.synchronized {
    try{
      Some(dateFormat.parse(s))
    } catch {
      case e: Exception => None
    }
  }
}
