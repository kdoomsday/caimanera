package models.layouts

sealed trait LinkType {
  def glyphicon: Option[String]  // Icon to use
}

case object LinkAdd extends LinkType {
  val glyphicon = Some("glyphicon-plus")
}

case object LinkRemove extends LinkType {
  val glyphicon = Some("glyphicon-remove")
}

case object LinkEdit extends LinkType {
  val glyphicon = Some("glyphicon-pencil")
}

case object LinkExit extends LinkType {
  val glyphicon = Some("glyphicon-log-out")
}