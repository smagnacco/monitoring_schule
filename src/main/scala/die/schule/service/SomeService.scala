package die.schule.service

import die.schule.api.Definition.{Alarm, Alarms}

class SomeService(transformer: SomeTransformer) {
  def doSomething(alarm: Alarm): Alarm = {
    transformer.transform(alarm)
  }
  def doSomethingMore(alarms: List[Alarm]): List[Alarm] = {
    Thread.sleep(200)
    alarms.map(doSomething(_))
  }
}

object SomeService {
  def apply(transformer: SomeTransformer): SomeService = new SomeService(transformer)
}

class SomeTransformer {
  def transform(alarm: Alarm): Alarm = {
    Thread.sleep(100)
    alarm
  }
}

object SomeTransformer {
  def apply(): SomeTransformer = new SomeTransformer()
}

object SomeServiceBuilder {
  def apply(): SomeService = SomeService(SomeTransformer())
}