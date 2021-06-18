package die.schule

import java.util.concurrent.{ArrayBlockingQueue, BlockingQueue, Executors, LinkedBlockingQueue, SynchronousQueue, ThreadFactory, ThreadPoolExecutor, TimeUnit}

import com.typesafe.config.Config

object TaskServiceFactory {


  def createFixedSizeThreadPool(config: Config, sleepTime: Int, capacity: Int): TaskService = {
    val fixedTPEConfig = config.getConfig("monitor-alarm.fixed-thread-pool")
    val corePoolSize = fixedTPEConfig.getInt("cores")
    val maximumPoolSize = fixedTPEConfig.getInt("max-pool-size")
    val keepAliveTime = fixedTPEConfig.getLong("keep-alive")

    val taskQueu: BlockingQueue[Runnable] = createQueue(capacity)

    val threadPoolExecutor = new ThreadPoolExecutor(
      corePoolSize,
      maximumPoolSize,
      keepAliveTime,
      TimeUnit.SECONDS,
      taskQueu
    )

    TaskService(threadPoolExecutor, sleepTime)
  }

  private def createQueue(capacity: Int): BlockingQueue[Runnable] = {
    if (capacity == 0)
      new LinkedBlockingQueue[Runnable]()  //capacity == Max integer (almos unbounded queue)
      else
      new ArrayBlockingQueue[Runnable](capacity)
  }

  def createCachedThreadPool(sleepTime: Int, capacityInt: Int): TaskService = {
    val capacity = if (capacityInt == 0) Integer.MAX_VALUE else capacityInt
    val tpe: ThreadPoolExecutor = new ThreadPoolExecutor(0, capacity,
      60L, TimeUnit.SECONDS,
      new SynchronousQueue[Runnable]())
    TaskService( tpe, sleepTime)
  }

  def createNThreadPool(config: Config, sleepTime: Int): TaskService = {
    val nThreads = config.getInt("monitor-alarm.n-thread-pool.n-thread")
    val tpe: ThreadPoolExecutor = new ThreadPoolExecutor(nThreads,
      nThreads, 0L,
      TimeUnit.MILLISECONDS,
      new LinkedBlockingQueue[Runnable])

    TaskService( tpe, sleepTime)
  }

  def build(config: Config): TaskService = {
    val sleepTime = config.getInt("monitor-alarm.routes.sleep-time")
    val capacity = config.getInt("monitor-alarm.routes.capacity")

    config.getString("monitor-alarm.routes.thread-pool-strategy") match {
      case "fixed-thread-pool" => createFixedSizeThreadPool(config, sleepTime, capacity)
      case "cached-thread-pool" => createCachedThreadPool(sleepTime, capacity)
      case "n-thread-pool" => createNThreadPool(config, sleepTime)
    }
  }

}
