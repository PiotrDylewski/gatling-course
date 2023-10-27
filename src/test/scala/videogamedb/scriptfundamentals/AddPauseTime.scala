package videogamedb.scriptfundamentals

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class AddPauseTime extends Simulation {

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")

  val scn = scenario("Video Game DB - 3 calls")
    .exec(http("Get All Games")
      .get("/videogame"))
    .pause(1)

    .exec(http("Get Specific Game")
      .get("/videogame/1"))
    .pause(1, 2)

    .exec(http("Delete Specific Game")
      .delete("/videogame/1"))

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)

}
