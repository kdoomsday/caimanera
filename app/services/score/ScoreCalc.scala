package services.score

import models.Equipo
import scala.concurrent.Future

/**
  * User: Eduardo Barrientos
  * Date: 10/17/15
  * Time: 7:43 AM
  */
trait ScoreCalc[T] {
  def calcular(e: Equipo)(implicit ev: Numeric[T]): Future[T]
}
