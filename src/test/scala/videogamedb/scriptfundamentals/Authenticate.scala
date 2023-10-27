package videogamedb.scriptfundamentals

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class Authenticate extends Simulation {

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")

  def authenticate() = {
    exec(http("Authenticate")
      .post("/authenticate")
      .header("Content-Type", "application/json")
      .body(StringBody(
      "{\n  \"password\": \"admin\",\n  \"username\": \"admin\"\n}"))
      .check(jsonPath("$.token").saveAs("jwtToken")))
  }

  def createNewGame() = {
    exec(http("Create New Game")
      .post("/videogame")
      .header("Authorization", "Bearer #{jwtToken}")
      .header("Content-Type", "application/json")
      .body(StringBody(
      "{\n  \"category\": \"Fight\",\n  \"name\": \"Tekken\",\n  \"rating\": \"Mature\",\n  \"releaseDate\": \"2022-05-04\",\n  \"reviewScore\": 99\n}")))
  }

  val scn = scenario("Authenticate")
    .exec(authenticate())
    .exec(createNewGame())


  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}
