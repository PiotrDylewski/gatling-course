package videogamedb.scriptfundamentals

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class CodeReuse extends Simulation {

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")

  def getAllVideogames() = {
    exec(http("Get All Video Games")
      .get("/videogame")
      .check(status.is(200)))
  }

  def getSpecificGames(executions: Int) = {
    repeat(executions, "counter") {
      exec(http("Get Specific Game with ID: #{counter}")
        .get("/videogame/#{counter}")
        .check(status.in(200 to 210)))
    }
  }

  def checkSpecificGame(gameId: Int, gameName: String) = {
    exec(http("Get Specific Game: " + gameName)
      .get("/videogame/" + gameId)
      .check(status.in(200 to 210))
      .check(jsonPath("$.name").is(gameName)))
  }

  val scn = scenario("Code Reuse")
    .exec(getAllVideogames())
    .pause(1)
    .exec(getSpecificGames(7))
    .pause(1)
    .exec(checkSpecificGame(2, "Gran Turismo 3"))
    .pause(1)
    .exec(checkSpecificGame(4, "Super Mario 32"))

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}
