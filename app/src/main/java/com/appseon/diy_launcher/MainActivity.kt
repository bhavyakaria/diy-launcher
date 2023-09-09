package com.appseon.diy_launcher

import android.content.Intent
import android.content.pm.*
import android.os.Build
import android.os.Bundle
import android.os.Process
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.appseon.diy_launcher.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var resolvedApplist: List<ResolveInfo>
    lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

//        getInstalledApps2()
        fetchInstalledApps()
    }

    fun getInstalledApps2() {
        resolvedApplist = packageManager
            .queryIntentActivities(Intent(Intent.ACTION_MAIN,null)
                .addCategory(Intent.CATEGORY_LAUNCHER),0)
        val appList = ArrayList<AppBlock>()

        for (ri in resolvedApplist) {
            if(ri.activityInfo.packageName!=this.packageName) {
                val app = AppBlock(
                    ri.loadLabel(packageManager).toString(),
                    ri.activityInfo.loadIcon(packageManager),
                    ri.activityInfo.packageName
                )
                appList.add(app)
            }
        }
        println("Total Apps: ${appList.size}")
        for (app in appList) {
            println("App Name: ${app.appName}, Package Name: ${app.packageName}")
        }

//        mainBinding.appList.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL )
//        mainBinding.appList.adapter = Adapter(this).also {
//            it.passAppList(appList.sortedWith(
//                Comparator<AppBlock> { o1, o2 -> o1?.appName?.compareTo(o2?.appName?:"",true)?:0; }
//            ))
//        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun fetchInstalledApps() {
        val launcherApps = getSystemService(LauncherApps::class.java)

        // Get the list of launcher activity info
        val activityList = launcherApps.getActivityList(null, Process.myUserHandle())
        println("Total Size: ${activityList.size}")
        var appList = mutableListOf<LauncherActivityInfo>()
        for (info in activityList) {
            // Retrieve app information
            val appName = info.label.toString()
            val packageName = info.componentName.packageName
            val appIcon = info.getIcon(0) // You can specify icon size here
            println("App Name: ${appName}, Package Name: $packageName")
            // Do something with the app information (e.g., display it, store it, etc.)
            val isLaunchable: Boolean = isAppLaunchable(packageManager, packageName)
            if (isLaunchable){
                appList.add(info)
            }
        }

        mainBinding.appList.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL )
        mainBinding.appList.adapter = Adapter(this).also {
            it.passAppList(activityList)
        }
    }

    private fun isAppLaunchable(packageManager: PackageManager, packageName: String): Boolean {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        return launchIntent != null
    }

    fun getInstalledApps(): ArrayList<ApplicationInfo> {
        val launcherApps = ArrayList<ApplicationInfo>()
        val packageManager: PackageManager = packageManager
        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        for (appInfo in apps) {
            launcherApps.add(appInfo)
        }
        return launcherApps
    }
}