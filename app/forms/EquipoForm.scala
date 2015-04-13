package forms

import play.api.data.Form
import play.api.data.Forms._

object EquipoForm {
    val form = Form(
        mapping("nombre" -> nonEmptyText)(EquipoData.apply)(EquipoData.unapply))
        
    case class EquipoData(nombre: String)
}