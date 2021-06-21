package die.schule.util

import kamon.Kamon

trait KamonSpanHelper {
  def trace[T](operationName: String, thunk: => T): T = {
    val span = Kamon.spanBuilder(operationName).start()
    val result = thunk
    span.finish
    result
  }
}
