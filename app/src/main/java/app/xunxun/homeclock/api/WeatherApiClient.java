package app.xunxun.homeclock.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fengdianxun on 2017/2/13.
 */

public class WeatherApiClient {

    public static CaiYunWeatherApi get() {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.caiyunapp.com/v2/sUvoLm0Tz7Fj=KCl/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(CaiYunWeatherApi.class);
    }
}
