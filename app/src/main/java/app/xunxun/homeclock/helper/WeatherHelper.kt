package app.xunxun.homeclock.helper

import app.xunxun.homeclock.api.WeatherApi
import app.xunxun.homeclock.model.Weather
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherHelper {

    @Throws(Exception::class)
    suspend fun weather(cityNum: String): Weather? {

        val retrofit = Retrofit.Builder()
                .baseUrl("http://weatherapi.market.xiaomi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
        val api = retrofit.create(WeatherApi::class.java)
        return api.weather(0, 0, "weathercn:$cityNum",
                "weather20151024", "zUFJoAR2ZVrDy1vF3D07", false, "zh_cn", 5).await()
    }
}