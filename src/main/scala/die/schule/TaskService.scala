package die.schule

import java.util.concurrent.ThreadPoolExecutor

import scala.util.Random

class TaskService(threadPoolExecutor: ThreadPoolExecutor, sleepTime: Int) {
  def runNewTask(): String = {
    threadPoolExecutor.execute(
      () => {
        Thread.sleep(Random.between(0, sleepTime))
      }
    )
      s"Task submitted"
    }

    def getThreads(): String = {
      val text = s"""Thread:
         | - Active Count: ${threadPoolExecutor.getActiveCount}
         | - Task Completed: ${threadPoolExecutor.getCompletedTaskCount} / Task Count: ${threadPoolExecutor.getTaskCount}
         | - Thread Pool size: ${threadPoolExecutor.getPoolSize} - largest: ${threadPoolExecutor.getLargestPoolSize} - max: ${threadPoolExecutor.getMaximumPoolSize}
         | - Task Queue  size: ${threadPoolExecutor.getQueue}
         | - Core Pool size: ${threadPoolExecutor.getCorePoolSize}
         |""".stripMargin

      text.replace("\n", ",")
    }
}

object TaskService {
  def apply(threadPoolExecutor: ThreadPoolExecutor, sleepTime: Int): TaskService = {
    ThreadPoolManager.addTpe(threadPoolExecutor)
    new TaskService(threadPoolExecutor, sleepTime)
  }
}