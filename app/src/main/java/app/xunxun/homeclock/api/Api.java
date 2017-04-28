package app.xunxun.homeclock.api;

import app.xunxun.homeclock.model.Pic;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by fengdianxun on 2017/4/28.
 */

public interface Api {

    @GET("/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=zh-CN")
    Call<Pic> getPic();

}
