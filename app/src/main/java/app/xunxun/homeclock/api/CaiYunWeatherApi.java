package app.xunxun.homeclock.api;

import app.xunxun.homeclock.model.RealWeatherResp;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by fengdianxun on 2017/2/13.
 */

public interface CaiYunWeatherApi {

    @GET("{loaction}/realtime.json")
    Call<RealWeatherResp> realtime(@Path("loaction") String loaction);

}
