package die.schule

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import die.schule.ensemble.AlarmActor
import kamon.Kamon

import scala.util.Failure
import scala.util.Success

object MonitorAlarmApp extends StrictLogging {
  private def startHttpServer(routes: Route)(implicit system: ActorSystem[_]): Unit = {
    import system.executionContext

    val futureBinding = Http().newServerAt("localhost", 9290).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        logger.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        logger.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load()

    Kamon.init(config)
    logger.info("Kamon initialized correctly")

    val appName = "monitor-alarm"

    val rootBehavior: Behavior[Nothing] = Behaviors.setup[Nothing] { context =>
      createAlarmActor(appName, context)
    }
    val system = ActorSystem[Nothing](rootBehavior, "HelloAkkaHttpServer")
  }

  private def createAlarmActor(appName: String, context: ActorContext[Nothing]) = {
    val alarmActor: ActorRef[AlarmActor.Command] = context.spawn(AlarmActor(), "alarmActor")
    context.watch(alarmActor)

    val routes = new Routes(alarmActor, appName)(context.system)
    startHttpServer(routes.alarmRoutes)(context.system)

    Behaviors.empty
  }
}
