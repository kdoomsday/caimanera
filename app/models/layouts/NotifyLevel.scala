package models.layouts

sealed trait NotifyLevel {
  def name: String
  def alertClass: String
}

object NotifyLevel {
  def apply(level: String) = level match {
    case NotifyError.name   ⇒ NotifyError
    case NotifySuccess.name ⇒ NotifySuccess
    case NotifyInfo.name    ⇒ NotifyInfo
    case NotifyWarning.name ⇒ NotifyWarning
    case _                  ⇒ NotifyInfo
  }
  
  def levels = Seq(NotifyError, NotifyInfo, NotifySuccess, NotifyWarning)
}

case object NotifyError extends NotifyLevel {
  val name = "error"
  val alertClass = "alert-danger"
}

case object NotifySuccess extends NotifyLevel {
  val name = "success"
  val alertClass = "alert-success"
}

case object NotifyInfo extends NotifyLevel {
  val name = "info"
  val alertClass = "alert-info"
}

case object NotifyWarning extends NotifyLevel {
  val name = "warning"
  val alertClass = "alert-warning"
}