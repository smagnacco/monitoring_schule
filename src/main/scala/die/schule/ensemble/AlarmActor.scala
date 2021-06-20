package die.schule.ensemble

import java.time.LocalDateTime

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import die.schule.api.Definition.{Alarm, Alarms}
import die.schule.service.SomeService

object AlarmActor {
  // actor protocol
  sealed trait Command
  final case class GetAlarms(replyTo: ActorRef[Alarms]) extends Command
  final case class CreateAlarm(Alarm: Alarm, replyTo: ActorRef[CommandResponse]) extends Command
  final case class GetAlarm(id: String, replyTo: ActorRef[GetAlarmResponse]) extends Command
  final case class DeleteAlarm(id: String, replyTo: ActorRef[CommandResponse]) extends Command

  final case class GetAlarmResponse(maybeAlarm: Option[Alarm])
  final case class CommandResponse(response: String)

  def apply(someService: SomeService): Behavior[Command] = performActionOn(List.empty, Map.empty, 0, someService)

  private def performActionOn(domainAlarms: List[DomainAlarm], alarmsById: Map[String, Alarm], nextId: Int, someService: SomeService): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetAlarms(replyTo) =>
        val alarms = domainAlarms.map(domainAlarm => Alarm(domainAlarm.id))
        replyTo ! Alarms(someService.doSomethingMore(alarms))
        Behaviors.same

      case CreateAlarm(alarm, replyTo) =>
        replyTo ! CommandResponse(s"Alarm ${alarm.id} created.")
        val domainAlarm = alarm match {
          case Alarm(id, None) => DomainAlarm(id)
          case Alarm(id, Some(bytes) ) => DomainAlarm(id = id, bytesPayload = ArrayFiller.createRandomArrayOf(bytes))
        }
        performActionOn(domainAlarm :: domainAlarms, alarmsById.updated(nextId.toString, alarm), nextId + 1, someService)

      case GetAlarm(id, replyTo) =>
        val maybeAlarm = alarmsById.get(id)
        replyTo ! GetAlarmResponse(maybeAlarm.map(someService.doSomething(_)))
        Behaviors.same

      case DeleteAlarm(id, replyTo) =>
        replyTo ! CommandResponse(s"Alarm $id deleted.")
        performActionOn(domainAlarms.filterNot(_.id == id), alarmsById.removed(id), nextId, someService)
    }
}

case class DomainAlarm(id: String,
                       creationDate: LocalDateTime = LocalDateTime.now(),
                       bytesPayload: Array[Byte] = ArrayFiller.createRandomArrayOf(size = 20))

object ArrayFiller {
  def createRandomArrayOf(size: Int): Array[Byte] = Array.fill(size)((scala.util.Random.nextInt(256) - 128).toByte)
}