package services.score

import models.{Partido, Equipo}
import models.daos.PartidoDao

/**
 * Considera los ultimos n partidos y suma los puntos de la siguiente forma:
 * - Partido ganado: 3 puntos.
 * - Partido empatado: 1 punto.
 * - Partido perdido: 0 puntos.
 *
 * User: Eduardo Barrientos
 * Date: 10/17/15
 * Time: 12:44 PM
 */
class PointsScoreCalculator(private[this] val daoPartidos: PartidoDao) extends ScoreCalc[Int] {

  /** Puntos de un equipo en un partido */
  private[this] def puntosPartido(e: Equipo, p: Partido): Int = {
    val goles = if (e == p.casa) (p.golesCasa, p.golesVisitante)
                else (p.golesVisitante, p.golesCasa)

    if (goles._1 > goles._2) 3
    else if (goles._1 == goles._2) 1
    else 0
  }

  override def calcular(e: Equipo): Numeric[Int] = ???
}
