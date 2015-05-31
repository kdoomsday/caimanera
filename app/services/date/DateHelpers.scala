package services.date

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormat

/**
 * @author doomsday
 */
object DateHelpers {
  lazy val dateWithTimeFormat = DateTimeFormat.forPattern("dd-MM-yyyy hh:mm a")
  
  def dateWithTime(date: DateTime): String = dateWithTimeFormat.print(date)
}