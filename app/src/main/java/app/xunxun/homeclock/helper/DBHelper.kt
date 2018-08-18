package app.xunxun.homeclock.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.FileOutputStream

const val DB_NAME = "weather.db"

class DBHelper(val context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, 1) {

    init {
        copy()
    }

    override fun onCreate(db: SQLiteDatabase?) {
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }


    fun copy() {
        val dbFile = context!!.getDatabasePath(DB_NAME)
        if (!dbFile.exists()) {
            val inputStream = context.assets.open(DB_NAME)
            val outputStream = FileOutputStream(dbFile)
            val buffer = ByteArray(1024)
            while (inputStream.read(buffer) > 0) {
                outputStream.write(buffer)
            }
            outputStream.flush()
            outputStream.close()
            inputStream.close()
        }
    }
}