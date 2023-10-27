package videogamedb.scriptfundamentals

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class CheckResponseBodyAndExtract extends Simulation {

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")

  val scn = scenario("CheckResponseBodyAndExtract")
    .exec(http("Get Specific Game")
      .get("/videogame/1")
      .check(status.is(200), jsonPath("$.name").is("Resident Evil 4")))

    .exec(http("Get All Games")
      .get("/videogame")
      .check(jsonPath("$[1].id").saveAs("gameId")))
    .exec{ session => println(session); session}

    .exec(http("Get Game From Saved Id")
      .get("/videogame/#{gameId}")
      .check(jsonPath("$.name").is("Gran Turismo 3"))
      .check(bodyString.saveAs("responseBody")))
    .exec{ session => println("Wartość parametru responseBody: " + session("responseBody").as[String]); session}

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}
