package com.appseon.diy_launcher

import android.content.Intent
import android.content.pm.*
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.appseon.diy_launcher.databinding.ActivityMainBinding
import com.appseon.diy_launcher.utils.Utils.fetchInstalledApps
import com.appseon.diy_launcher.utils.Utils.isMyAppLauncherDefault


class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        window.statusBarColor = Color.TRANSPARENT
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)
        renderInstalledApps()
        println("Default Launcher: ${isMyAppLauncherDefault(context = applicationContext, packageManager = packageManager)}")
    }

    private fun renderInstalledApps() {
        val activityList = fetchInstalledApps(context = applicationContext, packageManager = packageManager)
        mainBinding.appList.layoutManager = GridLayoutManager(this, 4)
        mainBinding.appList.adapter = Adapter(this).also {
            it.passAppList(activityList)
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    override fun onDestroy() {
        val intent = Intent(Intent.ACTION_MAIN)
        val packageManager: PackageManager = packageManager
        for (resolveInfo in packageManager.queryIntentActivities(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), PackageManager.MATCH_DEFAULT_ONLY)) {
            if (packageName != resolveInfo.activityInfo.packageName)  //if this activity is not in our activity (in other words, it's another default home screen)
            {
                startActivity(intent)
            }
            break
        }
        super.onDestroy()
    }

    override fun onBackPressed() { }
}