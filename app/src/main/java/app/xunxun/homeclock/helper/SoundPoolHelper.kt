package app.xunxun.homeclock.helper

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build

/**
 * 整点报时声音类.
 */
class SoundPoolHelper(val context: Context) {
    var sounds = mutableMapOf<String, Int>()
    private var soundPool: SoundPool

    init {

        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            SoundPool.Builder()
                    .setMaxStreams(25)
                    .setAudioAttributes(audioAttributes)
                    .build()
        } else {
            SoundPool(14, AudioManager.STREAM_MUSIC, 0)
        }

    }

    fun load() {
        val fileNames = arrayListOf<String>()
        for (i in 1..12) {
            fileNames.add("clock_am$i")
            fileNames.add("clock_pm$i")
        }
        for (fileName in fileNames) {
            val res = context.resources.getIdentifier(fileName, "raw", context.packageName)
            val id = soundPool.load(context, res, 1)
            sounds[fileName] = id
        }

    }

    fun play(fileName: String) {
        sounds[fileName]?.let { soundPool.play(it, 1.0f, 1.0f, 100, 0, 1.0f) }
    }

    fun release() {
        sounds.clear()
        soundPool.release()
    }

}