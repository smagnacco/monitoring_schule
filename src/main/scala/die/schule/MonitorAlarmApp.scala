package die.schule

import java.util.concurrent.ThreadPoolExecutor

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
      ThreadPoolManager.shutdown()
      system.terminate()
      logger.info("shutdown Kamon shutdown")
      Kamon.stopModules()
    }
  }
}

object ThreadPoolManager {
  var tpes: List[ThreadPoolExecutor] = Nil
  def addTpe(tpe: ThreadPoolExecutor) = tpes = tpe :: tpes
  def shutdown() = tpes.foreach(_.shutdown())
}