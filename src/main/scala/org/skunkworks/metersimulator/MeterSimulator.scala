package org.skunkworks.metersimulator

import akka.actor.ActorSystem

import scala.util.Try

object MeterSimulator extends App {
  val DEFAULT_NUM_METERS = 10

  val numMeters = Try(System.getProperty("numMeters").toInt).getOrElse(DEFAULT_NUM_METERS)
  val system = ActorSystem("akka-meter-simulator")
  val actors = (1 to numMeters) map (n => system.actorOf(MeterActor.props, s"meter-actor-$n"))

  actors foreach ( _ ! MeterActor.Initialize)
}

