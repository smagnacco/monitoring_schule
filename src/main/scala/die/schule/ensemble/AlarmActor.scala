package die.schule.ensemble

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import die.schule.api.Definition.{Alarm, Alarms}

object AlarmActor {
  // actor protocol
  sealed trait Command
  final case class GetAlarms(replyTo: ActorRef[Alarms]) extends Command
  final case class CreateAlarm(Alarm: Alarm, replyTo: ActorRef[CommandResponse]) extends Command
  final case class GetAlarm(id: String, replyTo: ActorRef[GetAlarmResponse]) extends Command
  final case class DeleteAlarm(id: String, replyTo: ActorRef[CommandResponse]) extends Command

  final case class GetAlarmResponse(maybeAlarm: Option[Alarm])
  final case class CommandResponse(response: String)

  def apply(): Behavior[Command] = performActionOn(List.empty)

  private def performActionOn(alarms: List[Alarm]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetAlarms(replyTo) =>
        replyTo ! Alarms(alarms)
        Behaviors.same

      case CreateAlarm(alarm, replyTo) =>
        replyTo ! CommandResponse(s"Alarm ${alarm.id} created.")
        performActionOn(alarm :: alarms)

      case GetAlarm(id, replyTo) =>
        replyTo ! GetAlarmResponse(alarms.find(_.id == id))
        Behaviors.same

      case DeleteAlarm(id, replyTo) =>
        replyTo ! CommandResponse(s"Alarm $id deleted.")
        performActionOn(alarms.filterNot(_.id == id))
    }
}
