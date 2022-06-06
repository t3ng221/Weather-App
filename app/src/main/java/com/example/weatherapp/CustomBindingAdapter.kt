package com.example.weatherapp

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.weatherapp.networkpackage.getFormattedDate
import com.example.weatherapp.networkpackage.icon_prefix
import com.example.weatherapp.networkpackage.icon_suffix

@BindingAdapter("app:setDateTime")
fun setDateTime(tv: TextView, dt: Long){
    tv.text = getFormattedDate(dt, "EEE HH:mm")
}

@BindingAdapter("app:setIcon")
fun setIcon(imageView: ImageView, icon: String){
    val iconUrl = "$icon_prefix$icon$icon_suffix"
    Glide.with(imageView.context).load(iconUrl).into(imageView)
}