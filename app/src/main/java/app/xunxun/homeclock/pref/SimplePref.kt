package app.xunxun.homeclock.pref

import android.content.Context


/**
 * Created by fengdianxun on 2017/11/7.
 */

object SimplePref {

    fun create(context: Context): PrefService {
        return Pref(context).create(PrefService::class.java)
    }

}
