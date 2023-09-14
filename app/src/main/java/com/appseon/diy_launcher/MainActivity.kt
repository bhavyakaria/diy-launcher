package com.appseon.diy_launcher

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.appseon.diy_launcher.databinding.ActivityMainBinding
import com.appseon.diy_launcher.utils.Utils
import com.appseon.diy_launcher.utils.Utils.fetchInstalledApps
import com.appseon.diy_launcher.utils.Utils.isMyAppLauncherDefault


class MainActivity : AppCompatActivity() {
  private lateinit var mainBinding: ActivityMainBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mainBinding = ActivityMainBinding.inflate(layoutInflater)
    val view = mainBinding.root
    makeStatusBarTransparent()
    setContentView(view)
    renderInstalledApps()
    Utils.log("MainActivity", "Default Launcher: ${isMyAppLauncherDefault(context = applicationContext, packageManager = packageManager)}")
  }

  private fun renderInstalledApps() {
    val activityList = fetchInstalledApps(context = applicationContext, packageManager = packageManager)
    mainBinding.appList.layoutManager = GridLayoutManager(this, 4)
    mainBinding.appList.adapter = Adapter(this).also {
      it.passAppList(activityList)
    }
  }

  override fun onDestroy() {
    val intent = Intent(Intent.ACTION_MAIN)
    val packageManager: PackageManager = packageManager
    for (resolveInfo in packageManager.queryIntentActivities(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), PackageManager.MATCH_DEFAULT_ONLY)) {
      if (packageName != resolveInfo.activityInfo.packageName) {
        startActivity(intent)
      }
      break
    }
    super.onDestroy()
  }

  private fun makeStatusBarTransparent() {
    val window = window
    window.setFlags(
      WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
      WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )
  }

  override fun onBackPressed() { }
}