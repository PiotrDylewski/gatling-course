package videogamedb.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.util.Random

class BasicLoadSimulation extends Simulation {

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
    repeat(2) {
      feed(customFeeder)
        .exec(http("Get Specific Game: #{gameId}")
          .get("/videogame/#{gameId}")
          .check(status.in(200 to 210)))
    }
  }

  val scn = scenario("Basic Load Simulation")
    .exec(getAllVideogames())
    .pause(2)
    .exec(getSpecificGames())
    .pause(2)

  setUp(
    scn.inject(
      nothingFor(4),
      atOnceUsers(16),
      nothingFor(2),
      incrementUsersPerSec(8)
        .times(4)
        .eachLevelLasting(8)
        .separatedByRampsLasting(4)
        .startingFrom(4),
      rampUsers(64).during(4),
      nothingFor(2),
      rampUsersPerSec(16).to(64).during(4)
    )
  ).protocols(httpProtocol)
}
