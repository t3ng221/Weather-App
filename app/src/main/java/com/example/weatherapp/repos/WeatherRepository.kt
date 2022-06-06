package com.example.weatherapp.repos

import android.location.Location
import com.example.weatherapp.models.CurrentModel
import com.example.weatherapp.models.ForecastModel
import com.example.weatherapp.networkpackage.NetworkService
import com.example.weatherapp.networkpackage.weather_api_key

class WeatherRepository {
    suspend fun fetchCurrentWeatherData(location: Location, status: Boolean) : CurrentModel {
        val unit = if(status) "imperial" else "metric"
        val end_Url = "weather?lat=${location.latitude}&lon=${location.longitude}" +
                "&units=$unit&appid=$weather_api_key"
        return NetworkService.weatherServiceAPI.getCurrentWeatherData(end_Url)
    }
    suspend fun fetchForecastWeatherData(location: Location, status: Boolean) : ForecastModel {
        val unit = if(status) "imperial" else "metric"
        val end_Url = "forecast?lat=${location.latitude}&lon=${location.longitude}" +
                "&units=$unit&appid=$weather_api_key"
        return NetworkService.weatherServiceAPI.getForecastWeatherData(end_Url)
    }
}