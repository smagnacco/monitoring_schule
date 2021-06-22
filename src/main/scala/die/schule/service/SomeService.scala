package die.schule.service

import die.schule.api.Definition.Alarm
import kamon.Kamon

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class SomeService(transformer: SomeTransformer) {
  def doSomething(alarm: Alarm): Alarm = {
    Kamon.span("service-process-one-alarm"){
      Thread.sleep(10)
      transformer.transform(alarm)
    }
  }

  def doSomethingInParallel(alarms: List[Alarm]): List[Alarm] = {
    Kamon.span("service-process-alarms"){
      Thread.sleep(20)
      val doSomethingF: List[Future[Alarm]] = alarms.map(alarm => Future{doSomething(alarm)})

      Await.result(Future.sequence(doSomethingF), 1 seconds)
    }
  }

  def doSomethingMore(alarms: List[Alarm], isParallel: Boolean): List[Alarm] = {
    if (isParallel)
      doSomethingInParallel(alarms)
    else
      doSomethingMore(alarms)
  }

  def doSomethingMore(alarms: List[Alarm]): List[Alarm] = {
    Kamon.span("service-process-alarms"){
      Thread.sleep(20)
      alarms.map(doSomething(_)).toList
    }
  }
}

object SomeService {
  def apply(transformer: SomeTransformer): SomeService = new SomeService(transformer)
}

class SomeTransformer {
  def transform(alarm: Alarm): Alarm = {
    Kamon.span("transform-alarm") {
      Thread.sleep(15)
      alarm
    }
  }
}

object SomeTransformer {
  def apply(): SomeTransformer = new SomeTransformer()
}

object SomeServiceBuilder {
  def apply(): SomeService = SomeService(SomeTransformer())
}


