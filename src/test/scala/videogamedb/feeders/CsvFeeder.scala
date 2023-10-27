package videogamedb.feeders

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class CsvFeeder extends Simulation {
  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")

  val csvFeeder = csv("data/gameCsvFile.csv").circular

  def getSpecificVideoGame(executions: Int) = {
    repeat(executions) {
      feed(csvFeeder)
        .exec(http("Get Video Game By Name: #{gameName}")
          .get("/videogame/#{gameId}")
          .check(jsonPath("$.name").is("#{gameName}")))
    }
  }

  val scn = scenario("CSV Feeder")
    .exec(getSpecificVideoGame(4))


  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}
