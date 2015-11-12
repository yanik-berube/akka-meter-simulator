package org.skunkworks.metersimulator
import akka.actor.{ActorSystem, Props}

import scala.concurrent.duration._
import scala.util.Try

object MeterSimulator extends App {
  val DEFAULT_NUM_METERS = 1000
  val DEFAULT_NUM_DIGITS = 6
  val DEFAULT_BLURT_FREQ_IN_MINUTES = 5

  val numMeters = Try(System.getProperty("numMeters").toInt).getOrElse(DEFAULT_NUM_METERS)
  val numDigits = Try(System.getProperty("numDigits").toInt).getOrElse(DEFAULT_NUM_DIGITS)
  val blurtFreqInMinutes = Try(System.getProperty("blurtFreq").toInt).getOrElse(DEFAULT_BLURT_FREQ_IN_MINUTES)


  val system = ActorSystem("akka-meter-simulator")

  system.log.info(s"Initializing meter simulator\n" +
    s"\tNumber of meters: $numMeters\n" +
    s"\tNumber of digits on faceplate: $numDigits\n" +
    s"\tBlurt frequency in minutes: $blurtFreqInMinutes")

  val actors = (1 to numMeters) map { n =>
    system.actorOf(MeterActor.props(numDigits, blurtFreqInMinutes.minutes), s"meter-actor-$n")
  }

  actors foreach ( _ ! MeterActor.Initialize)
}

