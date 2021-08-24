package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.util.Constants
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _asteroidsList = MutableLiveData<List<Asteroid>>()
    val asteroidsList: LiveData<List<Asteroid>>
        get() = _asteroidsList

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _navigateToDetails = MutableLiveData<Asteroid?>()
    val navigateToDetails
        get() = _navigateToDetails

    init{
        getAsteroids()
        getPictureOfDay()
    }

    fun onAsteroidClicked(asteroid: Asteroid){
        _navigateToDetails.value = asteroid
    }

    fun onDetailsNavigated() {
        _navigateToDetails.value = null
    }

    private fun getAsteroids(){
        viewModelScope.launch {
            try{
                val asteroidsString = Network.asteroids.getAsteroidsList(
                    getNextSevenDaysFormattedDates()[0],
                    getNextSevenDaysFormattedDates()[getNextSevenDaysFormattedDates().size-2],
                    Constants.API_KEY)
                val json = JSONObject(asteroidsString)
                _asteroidsList.postValue(parseAsteroidsJsonResult(json))
            } catch (e: Exception) {
                Log.e("Error getting Asteroids", e.toString())
            }
        }
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            try {
                _pictureOfDay.value = Network.pictureOfDay.getPictureOfDay(Constants.API_KEY)
            } catch (e: Exception) {
                Log.e("Error Picture of Day", e.toString())
            }
        }
    }

}
