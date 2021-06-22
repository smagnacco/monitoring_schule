package die.schule.util

import kamon.Kamon
import kamon.trace.Span

trait KamonSpanHelper {
  def trace[T](operationName: String, thunk: => T): T = {
    val span = Kamon.spanBuilder(operationName).
      asChildOf(Kamon.currentContext().get(Span.Key))
        .start()
    val result = thunk
    span.finish
    result
  }
}
