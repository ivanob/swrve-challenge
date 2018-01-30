import java.util.Date

case class Stats(totalUsers: Int, totalUsers640x960: Int, totalDollars: Int, idFirstUser: String, dateFirtsUser: Date){
  override def toString: String = {
    val strDateFirstUser = dateFirtsUser.toString
    s"-Total users: $totalUsers\n-Total users 640x960: $totalUsers640x960\n" +
      s"-Total dollars: $totalDollars\n-First user joined: $idFirstUser ($strDateFirstUser)"
  }
}
