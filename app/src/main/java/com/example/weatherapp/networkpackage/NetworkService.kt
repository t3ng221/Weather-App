package com.example.weatherapp.networkpackage

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.example.weatherapp.models.CurrentModel
import com.example.weatherapp.models.ForecastModel
import com.google.android.gms.location.FusedLocationProviderClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import java.text.SimpleDateFormat
import java.util.*

const val weather_api_key = "98a095ad234f2f0ad0448fc72550ac07"
const val base_url = "https://api.openweathermap.org/data/2.5/"
const val icon_prefix = "https://openweathermap.org/img/wn/"
const val icon_suffix = "@2x.png"

fun  getFormattedDate(dt: Long, pattern: String) =
    SimpleDateFormat(pattern).format(Date(dt*1000))

@SuppressLint("MissingPermission")
fun getLocation(context: Context, callback: (Location) -> Unit){
    val provider = FusedLocationProviderClient(context)
    provider.lastLocation.addOnSuccessListener {
        it?.let {
            callback(it)
        }
    }
}

val retrofit = Retrofit.Builder()
    .baseUrl(base_url)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface WeatherServiceAPI{
    @GET
    suspend fun getCurrentWeatherData(@Url endUrl: String) : CurrentModel

    @GET
    suspend fun getForecastWeatherData(@Url endUrl: String) : ForecastModel
}

object NetworkService {
    val weatherServiceAPI : WeatherServiceAPI by lazy {
        retrofit.create(WeatherServiceAPI::class.java)
    }
}
