package finaltest.customdatafeeders

import io.gatling.core.Predef.feed
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.util.Random

object CustomDataFeeder {

  var idNumbers = (1 to 10000).iterator
  val random = new Random()
  val now = LocalDate.now()
  val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  def randomString(length: Int) = {
    random.alphanumeric.filter(_.isLetter).take(length).mkString
  }

  def randomDate(startDate: LocalDate, random: Random): String = {
    startDate.minusDays(random.nextInt(30)).format(pattern)
  }

  val customCreateGameFeeder = Iterator.continually(Map(
    "gameId" -> idNumbers.next(),
    "name" -> ("Game-" + randomString(16)),
    "releaseDate" -> randomDate(now, random),
    "reviewScore" -> random.nextInt(100),
    "category" -> ("Category-" + randomString(8)),
    "rating" -> ("Rating-" + randomString(8))
  ))

  val customGetRandomGameFeeder = Iterator.continually(Map(
    "randomGameId" -> random.between(1, 11)
  ))

  def initCustomCreateGameFeeder = {
    feed(customCreateGameFeeder)
  }

  def initCustomGetRandomGameFeeder = {
    feed(customGetRandomGameFeeder)
  }
}
