package videogamedb.feeders

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.util.Random

class ComplexCustomFeeder extends Simulation {

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

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

  val customFeeder = Iterator.continually(Map(
    "gameId" -> idNumbers.next(),
    "name" -> ("Game-" + randomString(16)),
    "releaseDate" -> randomDate(now, random),
    "reviewScore" -> random.nextInt(100),
    "category" -> ("Category-" + randomString(8)),
    "rating" -> ("Rating-" + randomString(8))
  ))

  def authenticate() = {
    exec(http("Authenticate")
      .post("/authenticate")
      .body(StringBody(
        "{\n  \"password\": \"admin\",\n  \"username\": \"admin\"\n}"))
      .check(jsonPath("$.token").saveAs("jwtToken")))
  }

  def createNewGame(executions: Int) = {
    repeat(executions) {
      feed(customFeeder)
        .exec(http("Create New Game: #{name}")
          .post("/videogame")
          .header("Authorization", "Bearer #{jwtToken}")
          .body(ElFileBody("bodies/newGameTemplate.json")).asJson
          .check(bodyString.saveAs("responseBody")))
        .exec{ session => println(session("responseBody").as[String]); session}
    }
  }

  val scn = scenario("Complex Custom Feeder")
    .exec(authenticate())
    .exec(createNewGame(10))

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}
