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
import com.udacity.asteroidradar.repository.AsteroidsFilter
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.util.Constants
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    val pictureOfDay = asteroidsRepository.pictureOfDay

    private val _navigateToDetails = MutableLiveData<Asteroid?>()
    val navigateToDetails
        get() = _navigateToDetails

    private val _asteroidsList = MutableLiveData<List<Asteroid>>()
    val asteroidsList: LiveData<List<Asteroid>>
        get() = _asteroidsList

    private val asteroidListObserver = Observer<List<Asteroid>> {
        _asteroidsList.value = it
    }

    private var asteroidListLiveData: LiveData<List<Asteroid>>

    init{
        asteroidListLiveData =
            asteroidsRepository.getAsteroids(AsteroidsFilter.SHOW_WEEK)
        asteroidListLiveData.observeForever(asteroidListObserver)
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

    fun updateFilter(filter: AsteroidsFilter){
        asteroidListLiveData =
            asteroidsRepository.getAsteroids(filter)
        asteroidListLiveData.observeForever(asteroidListObserver)
    }


    override fun onCleared() {
        super.onCleared()
        asteroidListLiveData.removeObserver(asteroidListObserver)
    }
}
