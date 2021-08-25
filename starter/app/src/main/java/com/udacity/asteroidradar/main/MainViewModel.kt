package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.util.Constants
import kotlinx.coroutines.launch
import org.json.JSONObject

enum class ApiStatus { LOADING, ERROR, DONE }

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

//    private val _asteroidsList = MutableLiveData<List<Asteroid>>()
//    val asteroidsList: LiveData<List<Asteroid>>
//        get() = _asteroidsList

    val asteroidsList = asteroidsRepository.asteroids

//    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
//    val pictureOfDay: LiveData<PictureOfDay>
//        get() = _pictureOfDay

    val pictureOfDay = asteroidsRepository.pictureOfDay

    private val _navigateToDetails = MutableLiveData<Asteroid?>()
    val navigateToDetails
        get() = _navigateToDetails

    init{
//        getAsteroids()
//        getPictureOfDay()
        refreshAsteroid()
        refreshPicOfDay()
    }

    fun onAsteroidClicked(asteroid: Asteroid){
        _navigateToDetails.value = asteroid
    }

    fun onDetailsNavigated() {
        _navigateToDetails.value = null
    }

    private fun refreshAsteroid() {
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()
        }
    }

    private fun refreshPicOfDay() {
        viewModelScope.launch {
            asteroidsRepository.refreshPictureOfDay()
        }
    }

//    private fun getAsteroids(){
//        viewModelScope.launch {
//            _status.value = ApiStatus.LOADING
//            try{
//                val asteroidsString = Network.asteroids.getAsteroidsList(
//                    getNextSevenDaysFormattedDates()[0],
//                    getNextSevenDaysFormattedDates()[getNextSevenDaysFormattedDates().size-2],
//                    Constants.API_KEY)
//                val json = JSONObject(asteroidsString)
//                _asteroidsList.postValue(parseAsteroidsJsonResult(json))
//                _status.value = ApiStatus.DONE
//            } catch (e: Exception) {
//                Log.e("Error getting Asteroids", e.toString())
//                _status.value = ApiStatus.ERROR
//            }
//        }
//    }

//    private fun getPictureOfDay() {
//        viewModelScope.launch {
//            _status.value = ApiStatus.LOADING
//            try {
//                _pictureOfDay.value = Network.pictureOfDay.getPictureOfDay(Constants.API_KEY)
//                _status.value = ApiStatus.DONE
//            } catch (e: Exception) {
//                Log.e("Error Picture of Day", e.toString())
//                _status.value = ApiStatus.ERROR
//            }
//        }
//    }

}
