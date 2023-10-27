package videogamedb.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.util.Random

class FixedDurationLoadSimulation extends Simulation {

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  var random = new Random()

  val customFeeder = Iterator.continually(Map(
    "gameId" -> random.between(1, 11)
  ))

  def getAllVideogames() = {
    exec(http("Get All Video Games")
      .get("/videogame")
      .check(status.is(200)))
  }

  def getSpecificGames() = {
    repeat(1) {
      feed(customFeeder)
        .exec(http("Get Specific Game: #{gameId}")
          .get("/videogame/#{gameId}")
          .check(status.in(200 to 210)))
    }
  }

  val scn = scenario("Fixed Duration Load Simulation")
    .forever {                       //Scenario never stops
      exec(getAllVideogames())
        .pause(2)
        .exec(getSpecificGames())
        .pause(2)
    }

  setUp(
    scn.inject(
      nothingFor(4),
      atOnceUsers(16),         // Starts with 16 users at 0 sec
      rampUsers(16).during(32) // Adds another 16 users during 32 seconds so at 32 sec there are 32 active users
    )
  ).protocols(httpProtocol)
    .maxDuration(60)          // Scenario ends after 60 seconds
}
