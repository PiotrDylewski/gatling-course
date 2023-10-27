package videogamedb.scriptfundamentals

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class CheckResponseCode extends Simulation {

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")

  val scn = scenario("Check Response Codes")
    .exec(http("Get All Games")
      .get("/videogame")
      .check(status.is(200)))
    .pause(1)

    .exec(http("Get Specific Game")
      .get("/videogame/1")
      .check(status.in(200 to 210)))
    .pause(1, 2)

    .exec(http("Delete Specific Game")
      .delete("/videogame/1")
      .check(status.not(403), status.not(500)))

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)

}
