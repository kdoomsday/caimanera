package forms

import play.api.data.Form
import play.api.data.Forms._
import java.util.UUID
import org.joda.time.DateTime
import java.util.Formatter.DateTime
import org.joda.time.DateTime
import java.util.Date

object PartidoForm {
  
  case class PartidoData(
      casa: UUID,
      visitante: UUID,
      golesCasa: Int,
      golesVisitante: Int,
      fecha: Date
  )
  
  
  val form = Form(mapping(
      "casa" -> nonEmptyText,
      "visitante" -> nonEmptyText,
      "golesCasa" -> number(min = 0),
      "golesVisitante" -> number(min = 0),
      "fecha" -> date
      )
      ((c: String, v: String, gc: Int, gv: Int, f: Date) =>
          PartidoData(
              UUID.fromString(c),
              UUID.fromString(v),
              gc,
              gv,
              f ) )
      (pd => Some(pd.casa.toString, pd.visitante.toString, pd.golesCasa, pd.golesVisitante, pd.fecha))
  )
}