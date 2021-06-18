package die.schule

import akka.actor.typed.ActorSystem
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import die.schule.ensemble.Ensemble
import kamon.Kamon

object MonitorAlarmApp extends App with StrictLogging {
  val config = ConfigFactory.load()

  Kamon.init(config)
  logger.info("Kamon initialized correctly")
  logger.info(s"Starting Application ${Ensemble.appName}")

  val system = Ensemble.getActorSystem(TaskServiceFactory.build(config))

  shutdownHook(system)

  def shutdownHook(value: ActorSystem[Nothing]): Unit = {
    sys.addShutdownHook {
      logger.info("Shutdown actorSystem")
      system.terminate()
      logger.info("shutdown Kamon shutdown")
      Kamon.stopModules()
    }
  }
}
