package finaltest.httpcalls

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import finaltest.customdatafeeders.CustomDataFeeder._

object HttpCalls {

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  def authenticate() = {
    exec(http("Authenticate")
      .post("/authenticate")
      .body(StringBody(
        "{\n  \"password\": \"admin\",\n  \"username\": \"admin\"\n}"))
      .check(jsonPath("$.token").saveAs("jwtToken")))
  }

  def getAllVideogames() = {
    exec(http("Get All Video Games")
      .get("/videogame")
      .check(status.is(200))
    )
  }

  def getRandomSpecificGame() = {
    repeat(1) {
      exec(initCustomGetRandomGameFeeder)
        .exec(http("Get Specific Game: #{randomGameId}")
          .get("/videogame/#{randomGameId}")
          .check(status.in(200 to 210)))
    }
  }

  def deleteRandomSpecificGame() = {
    repeat(1) {
      exec(initCustomGetRandomGameFeeder)
        .exec(http("Delete Specific Game: #{randomGameId}")
          .delete("/videogame/#{randomGameId}")
          .header("Authorization", "Bearer #{jwtToken}")
          .check(status.in(200 to 210)))
//          .check(bodyString.saveAs("responseBody")))
//        .exec{ session => println(session("responseBody").as[String]); session}
    }
  }

  def createNewGame(executions: Int) = {
    repeat(executions) {
      exec(initCustomCreateGameFeeder)
        .exec(http("Create New Game: #{name}")
          .post("/videogame")
          .header("Authorization", "Bearer #{jwtToken}")
          .body(ElFileBody("bodies/newGameTemplate.json")).asJson
          .check(bodyString.saveAs("responseBody")))
        .exec{ session => println(session("responseBody").as[String]); session}
    }
  }
}
