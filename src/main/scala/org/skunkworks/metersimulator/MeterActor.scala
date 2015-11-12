package org.skunkworks.metersimulator

import akka.actor.{Props, Actor, ActorLogging}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global //TODO use a better tuned EC

class MeterActor(numDigits: Int = 6, blurtFreq: FiniteDuration = 5.minute) extends Actor with ActorLogging {
  import org.skunkworks.metersimulator.MeterActor._

  private val meterId = uuid
  private val rolloverNum = java.lang.Math.pow(10, numDigits).toInt
  private val blurtFreqInSec = blurtFreq.toSeconds

  //TODO: O(n) - kind of crappy, can we do better?
  private def lastBlurtTsFrom(timeInSec: Long): Long =
    ((timeInSec - blurtFreqInSec) to timeInSec).find(_ % blurtFreqInSec == 0).get
  private def nowInSec(): Long = System.currentTimeMillis() / 1000

  private def lastBlurtTs() = lastBlurtTsFrom(nowInSec())

  private def scheduleNextBlurtForDelay(delay: FiniteDuration)(prevReading: Int): Unit = {
    context.system.scheduler.scheduleOnce (delay, self, Blurt(prevReading))
  }

  private def scheduleNextBlurt = scheduleNextBlurtForDelay(blurtFreq)(_)

  def receive: Receive = {
    case Initialize =>
      val initialRead = randomRead
      val delay = scala.util.Random.nextInt(blurtFreqInSec.toInt)
      log.info(s"Initializing meter $meterId with base reading $initialRead starting to blurt in $delay seconds every ${blurtFreq.toSeconds} seconds")
      scheduleNextBlurtForDelay(delay.seconds)(initialRead)

    case Blurt(prevReading: Int) =>
      // for now, just log
      val newReading = (prevReading + randomIncr) % rolloverNum
      val ts = lastBlurtTs()
      log.info(s"Meter $meterId blurting $newReading for timestamp $ts")
      scheduleNextBlurt(newReading)
  }
}

object MeterActor {
  private val MAX_INIT_READ_VALUE = 1000

  def props(numDigits: Int = 6, blurtFreq: FiniteDuration = 5.minute): Props = Props(new MeterActor(numDigits, blurtFreq))

  private def uuid: String = java.util.UUID.randomUUID().toString
  private def randomRead = scala.util.Random.nextInt(MAX_INIT_READ_VALUE)
  private def randomIncr = scala.util.Random.nextInt(50)

  /** public messages */
  case object Initialize

  /** internal messages */
  case class Blurt(previousReading: Int)
}
