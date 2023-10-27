package finaltest.finalsimulation

import io.gatling.core.Predef._
import finaltest.httpcalls.HttpCalls._

class VideoGameFullTest extends Simulation {

  val USER_COUNT = System.getProperty("USER_COUNT","4").toInt
  val RAMP_DURATION = System.getProperty("RAMP_DURATION","8").toInt
  val TEST_DURATION = System.getProperty("TEST_DURATION","32").toInt

  after {
    println("********************************")
    println(s"Number of users: ${USER_COUNT}")
    println(s"Users ramp time (seconds): ${RAMP_DURATION}")
    println(s"Test duration (seconds): ${TEST_DURATION}")
    println("********************************")
  }

  /*** SCENARIO ***/
  val scn = scenario("Video Game Full Test")
    .forever {
      exec(authenticate())
        .exec(getAllVideogames())
        .exec(createNewGame(1))
        .exec(getRandomSpecificGame())
        .exec(deleteRandomSpecificGame())
    }

  /*** SETUP LOAD SIMULATION ***/
  setUp(
    scn.inject(
      rampUsers(USER_COUNT).during(RAMP_DURATION)
    )
  ).protocols(httpProtocol)
    .maxDuration(TEST_DURATION)
}
