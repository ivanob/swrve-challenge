import DownloadActor.DownloadCSV
import WorkerActor.ProcessLines
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Main {
  implicit val timeout = Timeout(5 seconds)

  def main(args: Array[String]): Unit = {
    val system: ActorSystem = ActorSystem("ProcessCSVSystem")
    val downloader: ActorRef = system.actorOf(DownloadActor.props("DownloadActor"), "downloaderActor")
    val futureBatches = downloader ? DownloadCSV
    val batches = Await.result(futureBatches, timeout.duration).asInstanceOf[List[List[String]]]
    val futureWorkers = batches.map(lines => {
      val workerActor = system.actorOf(Props(new WorkerActor(lines)))
      workerActor ? ProcessLines
    })
    val futures = Future.sequence(futureWorkers)
    val result = Await.result(futures, timeout.duration).asInstanceOf[List[Stats]]
    val finalStats = result.reduceLeft(Utils.statsReducer)
    println(finalStats)
    system.terminate()
  }

}
