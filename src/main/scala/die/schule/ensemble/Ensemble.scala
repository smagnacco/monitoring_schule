package die.schule.ensemble

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.http.scaladsl.server.{Route, RouteConcatenation}
import die.schule.service.{SomeService, SomeServiceBuilder}
import die.schule.{AlarmHttpServerFactory, AlarmRoutes, PostRoute, TaskService, TasksRoutes}

object Ensemble {
  val appName =  "monitor-alarm"

  private var maybeSystem: Option[ActorSystem[Nothing]] = None

  def getActorSystem(taskService: TaskService): ActorSystem[Nothing] = {
    if (maybeSystem.isEmpty) {
      val sys = createActorSystem(taskService)
      maybeSystem = Some(sys)
      sys
    } else {
      maybeSystem.get
    }
  }

  private def createActorSystem(taskService: TaskService): ActorSystem[Nothing] = {
    val rootBehavior: Behavior[Nothing] = Behaviors.setup[Nothing] { context =>
      RoutesFactory.createHttpServer(appName, context, taskService, SomeServiceBuilder())
    }
    ActorSystem[Nothing](rootBehavior, "MonitorAkkaHttpServer")
  }
}

object RoutesFactory {
  def createHttpServer(appName: String, context: ActorContext[Nothing],
                       taskService: TaskService, someService: SomeService): Behavior[Nothing] = {
    val alarmActor: ActorRef[AlarmActor.Command] = context.spawn(AlarmActor(someService), "alarmActor")
    context.watch(alarmActor)

    implicit val as = context.system.classicSystem
    val alarmRoutes = new AlarmRoutes(alarmActor, appName)(context.system)
    val taskRoutes = new TasksRoutes(taskService)
    val postRoute = new PostRoute()
    val routes: Route = RouteConcatenation.concat(taskRoutes.route,  alarmRoutes.route, postRoute.route)
    new AlarmHttpServerFactory().createHttpSever(routes)(context.system)

    Behaviors.empty
  }
}