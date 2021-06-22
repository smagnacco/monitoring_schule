package die.schule.service

import die.schule.api.Definition.Alarm
import die.schule.util.KamonSpanHelper
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.parallel.CollectionConverters._

class SomeService(transformer: SomeTransformer) extends KamonSpanHelper {
  def doSomething(alarm: Alarm): Alarm = {
    trace("service-process-one-alarm", {
      Thread.sleep(10)
      transformer.transform(alarm)
    })
  }

  def doSomethingInParallel(alarms: List[Alarm]): List[Alarm] = {
    trace("service-process-alarms", {
      Thread.sleep(20)
      alarms.par.map(doSomething(_)).toList
    })
  }

  def doSomethingMore(alarms: List[Alarm], isParallel: Boolean): List[Alarm] = {
    if (isParallel)
      doSomethingInParallel(alarms)
    else
      doSomethingMore(alarms)
  }

  def doSomethingMore(alarms: List[Alarm]): List[Alarm] = {
    trace("service-process-alarms", {
      Thread.sleep(20)
      alarms.map(doSomething(_)).toList
    })
  }
}

object SomeService {
  def apply(transformer: SomeTransformer): SomeService = new SomeService(transformer)
}

class SomeTransformer extends KamonSpanHelper {
  def transform(alarm: Alarm): Alarm = {
    trace("transform-alarm", {
      Thread.sleep(15)
      alarm
    })
  }
}

object SomeTransformer {
  def apply(): SomeTransformer = new SomeTransformer()
}

object SomeServiceBuilder {
  def apply(): SomeService = SomeService(SomeTransformer())
}


