package com.appseon.diy_launcher

import android.content.Context
import android.content.pm.LauncherActivityInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.appseon.diy_launcher.databinding.ItemAppBinding

class Adapter(val context: Context) : RecyclerView.Adapter<Adapter.AppItemViewHolder>() {

  lateinit var appBinding: ItemAppBinding
  var appList: MutableList<LauncherActivityInfo>?= null

  inner class AppItemViewHolder(
    val appBinding: ItemAppBinding
  ): RecyclerView.ViewHolder(appBinding.root)



  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppItemViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    appBinding = ItemAppBinding.inflate(inflater, parent, false)
    return AppItemViewHolder(appBinding)
  }

  override fun getItemCount(): Int {
    return appList?.size?:0
  }

  override fun onBindViewHolder(holder: AppItemViewHolder, position: Int) {
    holder.appBinding.appIcon.setImageDrawable(appList?.get(position)?.getIcon(0))
    holder.appBinding.appName.text = appList?.get(position)?.label
  }

  fun passAppList(
    appsList: MutableList<LauncherActivityInfo>
  ){
    appList = appsList
    notifyDataSetChanged()
  }
}