package die.schule

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, concat, get, onSuccess, pathEnd, pathPrefix, post}
import akka.http.scaladsl.server.Route
import die.schule.ensemble.AlarmActor.CommandResponse
import die.schule.json.JsonFormats

import scala.concurrent.Future

class TasksRoutes(taskService: TaskService) extends JsonFormats {
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  private def getThreads(): Future[CommandResponse] = {
    createResponse(taskService.getThreads())
  }

  private def runNewTask(): Future[CommandResponse] = {
    createResponse(taskService.runNewTask())
  }

  private def createResponse(response: String): Future[CommandResponse] = {
    Future.successful(CommandResponse(response))
  }

  val route: Route =
    pathPrefix("tasks") {
      concat(
        pathEnd {
          concat(
            get {
              onSuccess(getThreads()) { response =>
                complete((StatusCodes.Accepted, response))
              }
            },
            post {
                onSuccess(  runNewTask()  ) { response =>
                  complete((StatusCodes.Created, response))
                }
            })
        })
    }
}