package videogamedb

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class MyFirstTest extends Simulation {
  // 1. Http configuration
  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")


  // 2. Scenario definition
  val scn = scenario("My First Test")
    .exec(http("Get All Games")
      .get("/videogame"))


  // 3. Load scenario
  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)


}
