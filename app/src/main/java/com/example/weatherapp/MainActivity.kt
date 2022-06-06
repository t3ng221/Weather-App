package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.weatherapp.networkpackage.getLocation
import com.example.weatherapp.viewmodels.LocationViewModel
import com.google.android.gms.location.FusedLocationProviderClient

class MainActivity : AppCompatActivity() {

    private val locViewModel: LocationViewModel by viewModels()

    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                //Toast.makeText(this, "fine location granted", Toast.LENGTH_SHORT).show()
                detectUserLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                //Toast.makeText(this, "coarse location granted", Toast.LENGTH_SHORT).show()
                detectUserLocation()
            } else -> {
            Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show()
        }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))

    }

    @SuppressLint("MissingPermission")
    private fun detectUserLocation(){
        getLocation(this){
            locViewModel.setNewLocation(it)
        }
    }
}

/*fun isLocationPermissionGranted(context: Context) : Boolean{
    return ContextCompat
        .checkSelfPermission(context,
            android.Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED &&
            ContextCompat
                .checkSelfPermission(context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
}*/

/*fun requestUserForLocationPermission(activity: Activity){
    activity.requestPermissions(
        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION), 111
    )
}*/