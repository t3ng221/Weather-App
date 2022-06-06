package com.example.weatherapp.viewmodels

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.models.CurrentModel
import com.example.weatherapp.models.ForecastModel
import com.example.weatherapp.repos.WeatherRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class LocationViewModel: ViewModel() {
    val repository = WeatherRepository()
    val locationLiveData: MutableLiveData<Location> = MutableLiveData()
    val currentModelLiveData : MutableLiveData<CurrentModel> = MutableLiveData()
    val forecastModelLiveData : MutableLiveData<ForecastModel> = MutableLiveData()

    fun setNewLocation(location: Location){
        locationLiveData.value = location
    }

    fun fetchData(status: Boolean){
        viewModelScope.launch {
            try {
                currentModelLiveData.value = repository.fetchCurrentWeatherData(locationLiveData.value!!, status)
                forecastModelLiveData.value = repository.fetchForecastWeatherData(locationLiveData.value!!, status)
            }catch (e: Exception){
                Log.d("LocationViewModel", e.localizedMessage)
            }
        }
    }
}