package videogamedb.commandline

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RuntimeParameters extends Simulation {

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  def USERCOUNT = System.getProperty("USERS","1").toInt
  def RAMPDURATION = System.getProperty("RAMP_DURATION","10").toInt
  def TESTDURATION = System.getProperty("TEST_DURATION","60").toInt

  before {
    println(s"Number of users: ${USERCOUNT}")
    println(s"Users ramp time (seconds): ${RAMPDURATION}")
    println(s"Test duration (seconds): ${TESTDURATION}")
  }

  def getAllVideogames() = {
    exec(http("Get All Video Games")
      .get("/videogame")
      .check(status.is(200))
    ).pause(1)
  }

  val scn = scenario("Runtime Parameters")
    .forever {
      exec(getAllVideogames())
    }

  setUp(
    scn.inject(
      nothingFor(4),
      rampUsers(USERCOUNT).during(RAMPDURATION)
    )
  ).protocols(httpProtocol)
    .maxDuration(TESTDURATION)

}
