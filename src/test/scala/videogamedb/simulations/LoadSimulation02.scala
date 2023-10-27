package videogamedb.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.util.Random

class LoadSimulation02 extends Simulation {

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

  val scn = scenario("Load Simulation 02")
    .exec(getAllVideogames())
    .pause(2)
    .exec(getSpecificGames())
    .pause(2)

  setUp(
    scn.inject(
      nothingFor(4),
      rampUsersPerSec(10).to(20).during(10)
    )
  ).protocols(httpProtocol)
}
