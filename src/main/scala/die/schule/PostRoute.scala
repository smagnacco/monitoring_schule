package die.schule

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives.{complete, concat, get, onSuccess, pathEnd, pathPrefix}
import akka.http.scaladsl.server.Route
import die.schule.json.JsonFormats

import scala.concurrent.{ExecutionContext, Future}

class PostRoute(implicit as: ActorSystem)  extends JsonFormats {
  implicit val ec: ExecutionContext = as.dispatcher
  val uri = Uri("http://localhost:8080")

  def postToAnotherService(): Future[String] = {
    val r = HttpRequest(method = HttpMethods.POST,
      uri = uri, entity = "[{\"hello\":\"cruel world\"}]")

      execute(r).map(x => s"The result status: ${x.status}")
  }

  def execute(request: HttpRequest): Future[HttpResponse] = {
    Http().singleRequest(request)
  }

  val route: Route =
    pathPrefix("posts") {
      concat(
        pathEnd {
          concat(
            get {
              onSuccess(postToAnotherService()) { response =>
                complete((StatusCodes.Accepted, response))
              }
            })
        })
    }
}
