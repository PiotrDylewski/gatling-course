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
    println(s"Max Global Response Time (milliseconds): ${MAX_GLOBAL_RESPONSE_TIME}")
    println(s"Global Successful Response Rate (percent): ${GLOBAL_SUCCESSFUL_RESPONSE_PERCENT}")
    println("********************************")
    println("LOAD TEST FINISHED")
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
        global.responseTime.max.lt(MAX_GLOBAL_RESPONSE_TIME),
        global.successfulRequests.percent.gt(GLOBAL_SUCCESSFUL_RESPONSE_PERCENT)
      )
}
