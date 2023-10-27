package finaltest.config

object AppConfig {

  val USER_COUNT = System.getProperty("USER_COUNT","4").toInt
  val RAMP_DURATION = System.getProperty("RAMP_DURATION","8").toInt
  val TEST_DURATION = System.getProperty("TEST_DURATION","32").toInt

}