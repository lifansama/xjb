package app.xunxun.homeclock.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import app.xunxun.homeclock.R
import app.xunxun.homeclock.helper.UpdateHelper
import app.xunxun.homeclock.pref.SimplePref
import com.pgyersdk.feedback.PgyFeedback
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity

/**
 * 设置页面.
 */
class SettingsActivity : BaseActivity() {
    private var updateHelper: UpdateHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        init()

        updateHelper = UpdateHelper(this)
        styleTv.onClick { startActivity<StyleActivity>() }
        funcTv.onClick { startActivity<FuncActivity>() }
        supportTv.onClick { startActivity<SupportActivity>() }
        feedbackTv.onClick { PgyFeedback.getInstance().showDialog(this@SettingsActivity) }
        versionTv.onClick { updateHelper!!.check(true)  }
    }


    /**
     * 初始化设置.
     */
    private fun init() {


        try {
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT)
            versionTv!!.text = String.format("检查更新(v%s)", packageInfo.versionName)

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }


    }


    override fun onBackPressed() {
        if (SimplePref.create(this).isLauncher().get()) {
            val requestCode = intent.getIntExtra(REQUEST_CODE, -1)
            if (requestCode == REQUEST_MAIN) {
                MainActivity.start(this)
                finish()
            } else if (requestCode == REQUEST_LAUNCHER) {
                LauncherActivity.start(this)
                finish()
            } else {
                finish()
            }

        } else {
            MainActivity.start(this)
            finish()

        }
    }


    companion object {
        val REQUEST_CODE = "requestCode"
        val REQUEST_MAIN = 1
        val REQUEST_LAUNCHER = 2

        fun start(context: Context, requestCode: Int) {
            val intent = Intent(context, SettingsActivity::class.java)
            intent.putExtra(REQUEST_CODE, requestCode)
            context.startActivity(intent)
        }

    }

}
