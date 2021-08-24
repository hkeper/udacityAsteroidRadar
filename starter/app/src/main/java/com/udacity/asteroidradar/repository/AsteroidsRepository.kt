package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository (private val database: AsteroidsDatabase) {

//    val videos: LiveData<List<Asteroid>> =
//        Transformations.map(database.videoDao.getVideos()) {
//            it.asDomainModel()
//        }

        val asteroids: LiveData<List<Asteroid>> =
            Transformations.map(database.asteroidDao.getAsteroids()){
                it.asDomainModel()
            }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroidsString = Network.asteroids.getAsteroidsList(
                getNextSevenDaysFormattedDates()[0],
                getNextSevenDaysFormattedDates()[getNextSevenDaysFormattedDates().size-2],
                Constants.API_KEY)
            val json = JSONObject(asteroidsString)
            val asteroidsList = parseAsteroidsJsonResult(json)
            database.asteroidDao.insertAll( )
        }
    }
}