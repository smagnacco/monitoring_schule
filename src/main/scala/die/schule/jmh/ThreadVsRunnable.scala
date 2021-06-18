package die.schule.jmh

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations.{Benchmark, BenchmarkMode, Mode, OutputTimeUnit}

import scala.language.postfixOps

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Array(Mode.Throughput))
class ThreadVsRunnable {
  class ThreadImpl extends Thread("threadForJmh")

  @Benchmark
  def createThreadTest(): Unit = {
    val thread = new ThreadImpl() {
      Math.sin(1d)
    }
    thread.run()
  }

  @Benchmark
  def createRunnableTest(): Unit = {
    val runnable = new Runnable {
      override def run(): Unit = Math.sin(1d)
    }
    runnable.run()
  }


}
