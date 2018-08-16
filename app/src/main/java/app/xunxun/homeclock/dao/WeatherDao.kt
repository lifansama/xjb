package app.xunxun.homeclock.dao

import android.content.Context
import app.xunxun.homeclock.helper.DBHelper
import app.xunxun.homeclock.model.City
import java.io.Serializable

object WeatherDao {

    fun citys(context: Context, keyword: String): MutableList<City> {
        val list = mutableListOf<City>()
        if (keyword.isBlank()) return list
        val dbHelper = DBHelper(context)
        val sqLiteDatabase = dbHelper.readableDatabase
        val cursor = sqLiteDatabase.query("citys", null, "name like ?", arrayOf("%$keyword%"), null, null, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("_id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val cityNum = cursor.getString(cursor.getColumnIndex("city_num"))
            list.add(City(id, name, cityNum))
        }
        cursor.close()
        sqLiteDatabase.close()
        return list
    }

    fun city(context: Context, cityNum: String): City? {
        val dbHelper = DBHelper(context)
        val sqLiteDatabase = dbHelper.readableDatabase
        val cursor = sqLiteDatabase.query("citys", null, "city_num = ?", arrayOf(cityNum), null, null, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("_id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val cityNum = cursor.getString(cursor.getColumnIndex("city_num"))
            return City(id, name, cityNum)
        }
        cursor.close()
        sqLiteDatabase.close()
        return null

    }
}

