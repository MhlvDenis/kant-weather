package ru.hse.newsreader.other

object Credentials {

    private const val BASE_URL = "https://api.openweathermap.org/"

    private const val API_KEY = "20637400efd50be4cf42ae61c73a25ce"

    // HSE coordinates
    private const val LAT = 59.980240
    private const val LON = 30.326881

    private const val REQUEST_VERSION = "2.5"

    const val URL = "${BASE_URL}data/${REQUEST_VERSION}/weather?lat=${LAT}&lon=${LON}&appid=${API_KEY}"
}