package app.xunxun.homeclock.api

import android.content.Context
import android.support.annotation.Keep
import app.xunxun.homeclock.model.Weather
import app.xunxun.homeclock.model.WeatherInfo
import com.google.gson.Gson
import kotlinx.coroutines.experimental.Deferred
import org.apache.commons.io.IOUtils
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by fengdianxun on 2017/4/28.
 */

interface WeatherApi {

    @GET("wtr-v3/weather/all")
    fun weather(@Query("latitude") latitude: Int,
                @Query("longitude") longitude: Int,
                @Query("locationKey") locationKey: String,
                @Query("appKey") appKey: String,
                @Query("sign") sign: String,
                @Query("isGlobal") isGlobal: Boolean,
                @Query("locale") locale: String,
                @Query("days") days: Int): Deferred<Weather?>

}

fun Weather.Current.wea(context: Context): String? {
    val inputStream = context.assets.open("weather_status.json")
    val weatherJson = IOUtils.toString(inputStream, "utf-8")
    val weather = Gson().fromJson(weatherJson, WeatherInfo::class.java)
    for (wea in weather.weatherinfo) {
        if (wea.code == this.weather.toInt()) {
            return wea.wea
        }

    }
    return null
}
