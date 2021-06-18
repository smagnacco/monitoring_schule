package die.schule

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.typesafe.scalalogging.StrictLogging
import die.schule.api.Definition.{Alarm, Alarms}
import die.schule.ensemble.AlarmActor
import die.schule.ensemble.AlarmActor.{CommandResponse, CreateAlarm, DeleteAlarm, GetAlarm, GetAlarmResponse, GetAlarms}
import die.schule.json.JsonFormats
import kamon.instrumentation.akka.http.TracingDirectives

import scala.concurrent.Future

class AlarmRoutes(alarmActor: ActorRef[AlarmActor.Command], appName: String)(implicit val system: ActorSystem[_])
  extends JsonFormats with StrictLogging with TracingDirectives {
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  private implicit val timeout = Timeout.create(system.settings.config.getDuration(s"$appName.routes.request-timeout"))

  def getAlarms(): Future[Alarms] = {
    logger.info("get alarms!")
    alarmActor.ask(GetAlarms)
  }
  def getAlarm(id: String): Future[GetAlarmResponse] = {
    logger.info(s"get alarm by $id")
    alarmActor.ask(GetAlarm(id, _))
  }

  def createAlarm(alarm: Alarm): Future[CommandResponse] = {
    logger.info(s"create alarm $alarm")
    alarmActor.ask(CreateAlarm(alarm, _))
  }

  def deleteAlarm(id: String): Future[CommandResponse] = {
    logger.info(s"delete alarm $id")
    alarmActor.ask(DeleteAlarm(id, _))
  }
  val route: Route =
    pathPrefix("alarms") {
      concat(
        pathEnd {
          concat(
            get {
              complete(getAlarms())
            },
            post {
              entity(as[Alarm]) { Alarm =>
                onSuccess(  createAlarm(Alarm)  ) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        path(Segment) { id =>
          concat(
            get {
              rejectEmptyResponse {
                onSuccess(getAlarm(id)) { response =>
                  complete(response.maybeAlarm)
                }
              }
            },
            delete {
              onSuccess(deleteAlarm(id)) { performed =>
                complete((StatusCodes.OK, performed))
              }
            })
        })
    }

}
