package finaltest.appconfig

object AppConfig {

  val USER_COUNT = getProperty("USER_COUNT","4").toInt
  val RAMP_DURATION = getProperty("RAMP_DURATION","8").toInt
  val TEST_DURATION = getProperty("TEST_DURATION","30").toInt

  private def getProperty(propertyName: String, defaultValue: String) = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }
}