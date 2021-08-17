package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.domain.Asteroid

class MainViewModel : ViewModel() {

    //private var tonight = MutableLiveData<Asteroid?>()

    var list = mutableListOf<Asteroid>()

    private val _asteroidsList = MutableLiveData<List<Asteroid>>()

    val asteroidsList: LiveData<List<Asteroid>>
        get() = _asteroidsList

    private val _navigateToDetails = MutableLiveData<Asteroid?>()
    val navigateToDetails
        get() = _navigateToDetails

    private fun initialize() {
        list.add(Asteroid(11,
    "Demo Asteroid",
    "12.02.2022",
    233.0,
    44.0,
    43.0,
    35234.0,
    false))
        _asteroidsList.postValue(list)
    }

    init{
        initialize()
    }

    fun onAsteroidClicked(asteroid: Asteroid){
        _navigateToDetails.value = asteroid
    }

    fun onDetailsNavigated() {
        _navigateToDetails.value = null
    }

}