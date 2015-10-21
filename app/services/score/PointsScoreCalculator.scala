package services.score

import models.{Partido, Equipo}
import models.daos.PartidoDao
import scala.concurrent.Future

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
  import PointsScoreCalculator._

  /** Puntos de un equipo en un partido */
  private[this] def puntosPartido(e: Equipo)(p: Partido): Int = {
    val goles = if (e == p.casa) (p.golesCasa, p.golesVisitante)
                else (p.golesVisitante, p.golesCasa)

    if (goles._1 > goles._2)
      puntosGanar
    else if (goles._1 == goles._2)
      puntosEmpatar
    else
      puntosPerder
  }

  
  /** Calculo de puntos de los ultimos 10 partidos. */
  override def calcular(e: Equipo)(implicit ev: Numeric[Int]): Future[Int] = {
     import scala.concurrent.ExecutionContext.Implicits.global
     
     def extraerPuntos(partidos: Seq[Partido]): Seq[Int] = partidos.map(puntosPartido(e))
     
     val puntos =
       for {
         partidos <- daoPartidos.ultimosPartidos(e, partidosConsiderados)
       } yield extraerPuntos(partidos)
     
     puntos.map(_.sum)
  }
}


/** Constantes que usamos en los calculos */
object PointsScoreCalculator {
  val partidosConsiderados = 10
  
  val puntosGanar = 3
  val puntosEmpatar = 1
  val puntosPerder = 0
}