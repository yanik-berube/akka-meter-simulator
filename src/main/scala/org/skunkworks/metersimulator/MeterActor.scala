package org.skunkworks.metersimulator

import akka.actor.{Props, Actor, ActorLogging}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global //TODO use a better tuned EC

class MeterActor extends Actor with ActorLogging {
  import MeterActor._

  val DEFAULT_NUM_METERS = 10

  val meterId = uuid
  val numMeters = Option(System.getProperty("numMeters")).getOrElse(DEFAULT_NUM_METERS)

  // todo make duration configurable
  def scheduleNextBlurt(prevReading: Int, delay: FiniteDuration = 1.second): Unit =
    context.system.scheduler.scheduleOnce(delay, self, Blurt(prevReading))

  def receive: Receive = {
    case Initialize =>
      val initialRead = randomRead
      val delay = scala.util.Random.nextInt(60) // start blurting in the next minute
      log.info(s"Initializing meter $meterId with base reading $initialRead starting to blurt in $delay seconds")
      scheduleNextBlurt(initialRead)

    case Blurt(prevReading: Int) =>
      // for now, just log
      val newReading = prevReading + randomIncr
      val timestamp = System.currentTimeMillis() / 1000
      log.info(s"Meter $meterId blurting $newReading for timestamp $timestamp")
      scheduleNextBlurt(newReading)
  }
}

object MeterActor {
  private val MAX_INIT_READ_VALUE = 1000

  val props = Props[MeterActor]
  private def uuid: String = java.util.UUID.randomUUID().toString
  private def randomRead = scala.util.Random.nextInt(MAX_INIT_READ_VALUE)
  private def randomIncr = scala.util.Random.nextInt(50)

  /** public messages */
  case object Initialize

  /** internal messages */
  case class Blurt(previousReading: Int)
}
