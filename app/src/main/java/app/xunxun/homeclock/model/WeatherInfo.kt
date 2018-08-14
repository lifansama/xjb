package app.xunxun.homeclock.model


data class WeatherInfo(
        val weatherinfo: List<Weatherinfo>
) {

    data class Weatherinfo(
            val code: Int,
            val wea: String
    )
}