import java.util.Date

case class SanitizedLine(userId: Option[String], dateJoined: Option[Date], moneySpent: Option[Int],
                         msPlayed: Option[Int], deviceWidth: Option[Int], deviceHeight: Option[Int])
