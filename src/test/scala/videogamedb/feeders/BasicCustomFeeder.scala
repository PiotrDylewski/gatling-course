package videogamedb.feeders

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BasicCustomFeeder extends Simulation {

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")

  var counter = 20
  var idNumbers = (1 to counter).iterator

  val customFeeder = Iterator.continually(Map("gameId" -> idNumbers.next()))

  def getSpecificVideoGame(executions: Int) = {
    repeat(executions) {
      feed(customFeeder)
        .exec(http("Get Video Game With Custom Feeder Id: #{gameId}")
          .get("/videogame/#{gameId}")
          .check(status.is(200)))
    }
  }

  val scn = scenario("Basic Custom Feeder")
    .exec(getSpecificVideoGame(counter))

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}
