import java.util.{Calendar, Date}

object Utils {
  val now = Calendar.getInstance().getTime()

  val statsReducer = { (x:Stats,y:Stats) => {
    val newTotalUsers = x.totalUsers + y.totalUsers
    val newTotalUsers640x960 = x.totalUsers640x960 + y.totalUsers640x960
    val newTotalDollars = x.totalDollars + y.totalDollars
    val firstUser = if(x.dateFirtsUser.before(y.dateFirtsUser))
      (x.idFirstUser, x.dateFirtsUser) else (y.idFirstUser, y.dateFirtsUser)
    Stats(newTotalUsers,newTotalUsers640x960,newTotalDollars,firstUser._1,firstUser._2)
  }}

  val sanitizeLine = { (line: String) => {
    val chunks = line.split(",").toList
    val idUser = if(chunks.length>=1 && !chunks(0).isEmpty) Some(chunks(0)) else None
    val dateJoined: Option[Date] = if(chunks.length>=2) Conversion.toDate(chunks(1)) else None
    val moneySpent: Option[Int] = if(chunks.length>=3) Conversion.toInt(chunks(2)) else None
    val msPlayed: Option[Int] = if(chunks.length>=4) Conversion.toInt(chunks(3)) else None
    val deviceHeight: Option[Int] = if(chunks.length>=5) Conversion.toInt(chunks(4)) else None
    val deviceWidth: Option[Int] = if(chunks.length>=6) Conversion.toInt(chunks(5)) else None
    SanitizedLine(idUser, dateJoined, moneySpent, msPlayed, deviceWidth, deviceHeight)
  }}

  val sanitizedLineReducer = { (stats: Stats, line:SanitizedLine) => {
    val newTotalUsers = stats.totalUsers+1
    val newTotalUsers640x960 = if(line.deviceWidth.getOrElse(0)==640 && line.deviceHeight.getOrElse(0)==960)
      stats.totalUsers640x960+1 else stats.totalUsers640x960
    val newTotalDollars = stats.totalDollars + line.moneySpent.getOrElse(0)
    val firstUser = if(line.dateJoined.getOrElse(now).before(stats.dateFirtsUser)) (line.userId.getOrElse(""),line.dateJoined.getOrElse(now))
      else (stats.idFirstUser,stats.dateFirtsUser)
    Stats(newTotalUsers,newTotalUsers640x960,newTotalDollars,firstUser._1,firstUser._2)
  }}

  val getNullStats = {
    val now = Calendar.getInstance().getTime()
    Stats(0,0,0,"",now)
  }
}
