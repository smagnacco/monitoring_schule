package die.schule.api

import scala.collection.immutable.List

object Definition {
  final case class Alarm(id: String, bytes: Option[Int] = None)
  final case class Alarms(Alarms: List[Alarm])
}
