package com.appseon.diy_launcher.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.LauncherActivityInfo
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.os.Process
import android.util.Log

object Utils {

  fun log(tag: String, data: String) {
    Log.d(tag, data)
  }

  fun fetchInstalledApps(context: Context, packageManager: PackageManager): MutableList<LauncherActivityInfo> {
    val launcherApps = context.getSystemService(LauncherApps::class.java)

    // Get the list of launcher activity info
    val activityList = launcherApps.getActivityList(null, Process.myUserHandle())
    val appList = mutableListOf<LauncherActivityInfo>()
    log("MainActivity", "Total Apps: ${activityList.size}")
    for (info in activityList) {
      // Retrieve app information
      val packageName = info.componentName.packageName
      // Do something with the app information (e.g., display it, store it, etc.)
      val isLaunchable: Boolean = isAppLaunchable(packageManager, packageName)
      if (isLaunchable){
        appList.add(info)
      }
    }
    return activityList
  }

  private fun isAppLaunchable(packageManager: PackageManager, packageName: String): Boolean {
    val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
    return launchIntent != null
  }

  fun isMyAppLauncherDefault(context: Context, packageManager: PackageManager): Boolean {
    val filter = IntentFilter(Intent.ACTION_MAIN)
    filter.addCategory(Intent.CATEGORY_HOME)
    val filters: MutableList<IntentFilter> = ArrayList()
    filters.add(filter)
    val myPackageName = context.packageName
    val activities: List<ComponentName> = ArrayList()
    packageManager.getPreferredActivities(filters, activities, null)
    for (activity in activities) {
      if (myPackageName == activity.packageName) {
        return true
      }
    }
    return false
  }
}