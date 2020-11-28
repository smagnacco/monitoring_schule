package die.schule.api

import scala.collection.immutable.List

object Definition {
  final case class Alarm(id: String)
  final case class Alarms(Alarms: List[Alarm])
}
