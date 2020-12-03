package die.schule.ensemble

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import die.schule.{AlarmHttpServerFactory, Routes}

object Ensemble {
  val appName =  "monitor-alarm"

  private var maybeSystem: Option[ActorSystem[Nothing]] = None

  def getActorSystem(): ActorSystem[Nothing] = {
    if (maybeSystem.isEmpty) {
      val sys = createActorSystem()
      maybeSystem = Some(sys)
      sys
    } else {
      maybeSystem.get
    }
  }

  private def createActorSystem(): ActorSystem[Nothing] = {
    val rootBehavior: Behavior[Nothing] = Behaviors.setup[Nothing] { context =>
      AlarmActorFactory.createAlarmActor(appName, context)
    }
    ActorSystem[Nothing](rootBehavior, "MonitorAkkaHttpServer")
  }
}

object AlarmActorFactory {
  def createAlarmActor(appName: String, context: ActorContext[Nothing]): Behavior[Nothing] = {
    val alarmActor: ActorRef[AlarmActor.Command] = context.spawn(AlarmActor(), "alarmActor")
    context.watch(alarmActor)

    val routes = new Routes(alarmActor, appName)(context.system)

    new AlarmHttpServerFactory().createHttpSever(routes.alarmRoutes)(context.system)

    Behaviors.empty
  }
}