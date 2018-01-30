import java.util.Calendar

import WorkerActor.ProcessLines
import akka.actor.{Actor, Props}

object WorkerActor {
  def props(lines: List[String]): Props = Props(new WorkerActor(lines))

  final case object ProcessLines
}

class WorkerActor(lines: List[String]) extends Actor{
  def receive = {
    case ProcessLines => {
      val sanitizedLines = lines.map(l => Utils.sanitizeLine(l))
      sender() ! sanitizedLines.foldLeft(Utils.getNullStats)(Utils.sanitizedLineReducer)
    }
  }
}
