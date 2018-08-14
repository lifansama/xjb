package app.xunxun.homeclock.api

import android.content.Context
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


data class Weather(
        val current: Current,
        val forecastDaily: ForecastDaily,
        val forecastHourly: ForecastHourly,
        val indices: Indices,
        val aqi: Aqi,
        val alerts: List<Any>,
        val yesterday: Yesterday,
        val url: Url,
        val brandInfo: BrandInfo
) {

    data class Current(
            val feelsLike: FeelsLike,
            val humidity: Humidity,
            val pressure: Pressure,
            val pubTime: String,
            val temperature: Temperature,
            val uvIndex: String,
            val visibility: Visibility,
            val weather: String,
            val wind: Wind
    ) {

        data class Visibility(
                val unit: String,
                val value: String
        )


        data class FeelsLike(
                val unit: String,
                val value: String
        )


        data class Temperature(
                val unit: String,
                val value: String
        )


        data class Pressure(
                val unit: String,
                val value: String
        )


        data class Humidity(
                val unit: String,
                val value: String
        )


        data class Wind(
                val direction: Direction,
                val speed: Speed
        ) {

            data class Direction(
                    val unit: String,
                    val value: String
            )


            data class Speed(
                    val unit: String,
                    val value: String
            )
        }
    }


    data class ForecastHourly(
            val aqi: Aqi,
            val status: Int,
            val temperature: Temperature,
            val weather: Weather
    ) {

        data class Aqi(
                val brandInfo: BrandInfo,
                val pubTime: String,
                val status: Int,
                val value: List<Int>
        ) {

            data class BrandInfo(
                    val brands: List<Brand>
            ) {

                data class Brand(
                        val brandId: String,
                        val logo: String,
                        val names: Names,
                        val url: String
                ) {

                    data class Names(
                            val en_US: String,
                            val zh_TW: String,
                            val zh_CN: String
                    )
                }
            }
        }


        data class Temperature(
                val pubTime: String,
                val status: Int,
                val unit: String,
                val value: List<Int>
        )


        data class Weather(
                val pubTime: String,
                val status: Int,
                val value: List<Int>
        )
    }


    data class Yesterday(
            val aqi: String,
            val date: String,
            val status: Int,
            val sunRise: String,
            val sunSet: String,
            val tempMax: String,
            val tempMin: String,
            val weatherEnd: String,
            val weatherStart: String,
            val windDircEnd: String,
            val windDircStart: String,
            val windSpeedEnd: String,
            val windSpeedStart: String
    )


    data class Url(
            val weathercn: String,
            val caiyun: String
    )


    data class ForecastDaily(
            val aqi: Aqi,
            val precipitationProbability: PrecipitationProbability,
            val pubTime: String,
            val status: Int,
            val sunRiseSet: SunRiseSet,
            val temperature: Temperature,
            val weather: Weather,
            val wind: Wind
    ) {

        data class Aqi(
                val brandInfo: BrandInfo,
                val pubTime: String,
                val status: Int,
                val value: List<Int>
        ) {

            data class BrandInfo(
                    val brands: List<Brand>
            ) {

                data class Brand(
                        val brandId: String,
                        val logo: String,
                        val names: Names,
                        val url: String
                ) {

                    data class Names(
                            val en_US: String,
                            val zh_TW: String,
                            val zh_CN: String
                    )
                }
            }
        }


        data class SunRiseSet(
                val status: Int,
                val value: List<Value>
        ) {

            data class Value(
                    val from: String,
                    val to: String
            )
        }


        data class PrecipitationProbability(
                val status: Int,
                val value: List<String>
        )


        data class Wind(
                val direction: Direction,
                val speed: Speed
        ) {

            data class Direction(
                    val status: Int,
                    val unit: String,
                    val value: List<Value>
            ) {

                data class Value(
                        val from: String,
                        val to: String
                )
            }


            data class Speed(
                    val status: Int,
                    val unit: String,
                    val value: List<Value>
            ) {

                data class Value(
                        val from: String,
                        val to: String
                )
            }
        }


        data class Weather(
                val status: Int,
                val value: List<Value>
        ) {

            data class Value(
                    val from: String,
                    val to: String
            )
        }


        data class Temperature(
                val status: Int,
                val unit: String,
                val value: List<Value>
        ) {

            data class Value(
                    val from: String,
                    val to: String
            )
        }
    }


    data class BrandInfo(
            val brands: List<Brand>
    ) {

        data class Brand(
                val brandId: String,
                val logo: String,
                val names: Names,
                val url: String
        ) {

            data class Names(
                    val en_US: String,
                    val zh_TW: String,
                    val zh_CN: String
            )
        }
    }


    data class Indices(
            val indices: List<Indice>,
            val pubTime: String,
            val status: Int
    ) {

        data class Indice(
                val type: String,
                val value: String
        )
    }


    data class Aqi(
            val aqi: String,
            val brandInfo: BrandInfo,
            val co: String,
            val no2: String,
            val o3: String,
            val pm10: String,
            val pm25: String,
            val primary: String,
            val pubTime: String,
            val so2: String,
            val src: String,
            val status: Int
    ) {

        data class BrandInfo(
                val brands: List<Brand>
        ) {

            data class Brand(
                    val brandId: String,
                    val logo: String,
                    val names: Names,
                    val url: String
            ) {

                data class Names(
                        val en_US: String,
                        val zh_TW: String,
                        val zh_CN: String
                )
            }
        }
    }
}