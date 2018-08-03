package app.xunxun.homeclock.pref

import android.content.Context
import java.lang.reflect.Proxy

/**
 * Created by fengdianxun on 2017/11/20.
 */

class Pref(private val context: Context) {

    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(service.classLoader, arrayOf<Class<*>>(service)) { proxy, method, args ->
            val prefStringAnno = method.getAnnotation(PrefString::class.java)
            val prefIntAnno = method.getAnnotation(PrefInt::class.java)
            val prefLongAnno = method.getAnnotation(PrefLong::class.java)
            val prefBooleanAnno = method.getAnnotation(PrefBoolean::class.java)
            val prefBodyAnno = method.getAnnotation(PrefBody::class.java)
            return@newProxyInstance when {
                prefStringAnno != null -> Call(context, prefStringAnno.key,prefStringAnno.defaultValue)
                prefIntAnno != null -> Call(context, prefIntAnno.key,prefIntAnno.defaultValue)
                prefBooleanAnno != null -> Call(context, prefBooleanAnno.key,prefBooleanAnno.defaultValue)
                prefLongAnno != null -> Call(context, prefLongAnno.key,prefLongAnno.defaultValue)
                prefBodyAnno != null -> Call(context, prefBodyAnno.key,null)
                else -> throw Exception("Need Annotation")
            }

        } as T
    }

}
