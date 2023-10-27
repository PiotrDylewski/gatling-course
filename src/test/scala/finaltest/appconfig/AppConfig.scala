package finaltest.appconfig

object AppConfig {

  val USER_COUNT = getProperty("USER_COUNT","4").toInt
  val RAMP_DURATION = getProperty("RAMP_DURATION","8").toInt
  val TEST_DURATION = getProperty("TEST_DURATION","30").toInt
  val MAX_GLOBAL_RESPONSE_TIME = getProperty("MAX_GLOBAL_RESPONSE_TIME","512").toInt
  val GLOBAL_SUCCESSFUL_RESPONSE_PERCENT = getProperty("GLOBAL_SUCCESSFUL_RESPONSE_PERCENT","99").toInt

  private def getProperty(propertyName: String, defaultValue: String) = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }
}