package finaltest.finalsimulation

import io.gatling.core.Predef._
import finaltest.appconfig.AppConfig._
import finaltest.httpcalls.HttpCalls._

class VideoGameFullTest extends Simulation {

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
      .assertions(
        global.responseTime.max.lt(64),
        global.successfulRequests.percent.gt(99)
      )
}
