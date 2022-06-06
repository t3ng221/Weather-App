package com.example.weatherapp

import android.content.ContentValues
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.example.weatherapp.models.CurrentModel
import com.example.weatherapp.networkpackage.getFormattedDate
import com.example.weatherapp.networkpackage.getLocation
import com.example.weatherapp.networkpackage.icon_prefix
import com.example.weatherapp.networkpackage.icon_suffix
import com.example.weatherapp.preferences.WeatherPreference
import com.example.weatherapp.viewmodels.LocationViewModel
import kotlin.math.roundToInt

class WeatherFragment : Fragment() {

    private lateinit var preference: WeatherPreference
    private val locViewModel: LocationViewModel by activityViewModels()
    private lateinit var binding: FragmentWeatherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.weather_menu,menu)
        val searchView = menu.findItem(R.id.item_search).actionView as SearchView
        searchView.queryHint = "Search city by name"
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    convertQueryToLatLong(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.item_location){
            getLocation(requireContext()){
                locViewModel.setNewLocation(it)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun convertQueryToLatLong(query: String){
        val geocoder = Geocoder(requireActivity())
        val addressList: List<Address> = geocoder.getFromLocationName(query,1)
        if(addressList.isNotEmpty()){
            val lat = addressList[0].latitude
            val lng = addressList[0].longitude
            val location = Location("").apply {
                latitude = lat
                longitude = lng
            }
            locViewModel.setNewLocation(location)
        }else{
            Toast.makeText(requireActivity(), "Invalid city name!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        preference = WeatherPreference(requireContext())
        binding.tempSwitch.isChecked = preference.getTempUnitStatus()
        val adapter = ForecastAdapter()
        val llm = LinearLayoutManager(requireActivity())
        llm.orientation = LinearLayoutManager.HORIZONTAL
        binding.forecastRV.layoutManager = llm
        binding.forecastRV.adapter = adapter

        locViewModel.locationLiveData.observe(viewLifecycleOwner){
            /*Toast.makeText(requireActivity(), "${it.latitude},${it.longitude}", Toast.LENGTH_SHORT).show()*/
            locViewModel.locationLiveData.observe(viewLifecycleOwner){
                locViewModel.fetchData(preference.getTempUnitStatus())
            }
            locViewModel.currentModelLiveData.observe(viewLifecycleOwner){
                Log.d(ContentValues.TAG,"${it.main.temp}")
                setCurrentData(it)
            }
            locViewModel.forecastModelLiveData.observe(viewLifecycleOwner){
                Log.d(ContentValues.TAG,"${it.list.size}")
                adapter.submitList(it.list)
            }
        }
        binding.tempSwitch.setOnCheckedChangeListener { compoundButton, isOn ->
            preference.setTempUnitStatus(isOn)
            locViewModel.fetchData(isOn)
        }

        return binding.root
    }

    private fun setCurrentData(it: CurrentModel) {
        binding.showDateTV.text = getFormattedDate(it.dt, "MMM dd, yyyy HH:mm")
        binding.showCityTV.text = "${it.name}, ${it.sys.country}"
        val iconUrl = "$icon_prefix${it.weather[0].icon}${icon_suffix}"
        Glide.with(requireActivity()).load(iconUrl).into(binding.showIconIV)
        binding.showTempTV.text = "${it.main.temp.roundToInt().toString()}\u00B0"
        binding.showFeelTempTV.text = it.main.feels_like.roundToInt().toString()
        binding.showTempStatTV.text = it.weather[0].description
        binding.showMaxTempTV.text = "${it.main.temp_max.roundToInt().toString()}\u00B0"
        binding.showMinTempTV.text = "${it.main.temp_min.roundToInt().toString()}\u00B0"
        binding.showSunriseTV.text = getFormattedDate(it.sys.sunrise, "HH:mm")
        binding.showSunsetTV.text = getFormattedDate(it.sys.sunset, "HH:mm")

    }
}