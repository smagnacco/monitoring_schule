package die.schule.json

import die.schule.ensemble.AlarmActor.{CommandResponse, GetAlarmResponse}
import die.schule.api.Definition.{Alarm, Alarms}
import spray.json.DefaultJsonProtocol

trait JsonFormats  {
  import DefaultJsonProtocol._

  implicit val AlarmJsonFormat = jsonFormat2(Alarm)
  implicit val AlarmsJsonFormat = jsonFormat1(Alarms)

  implicit val CommandResponseJsonFormat = jsonFormat1(CommandResponse)
  implicit val GetAlarmResponseJsonFormat = jsonFormat1(GetAlarmResponse)
}
