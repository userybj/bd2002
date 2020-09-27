package utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatter {
  def dateFormat(date: String) = {
    val parser = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.US)
    val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    formatter.format(parser.parse(date))
  }
}
