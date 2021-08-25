package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.network.NetworkAsteroidContainer
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository (private val database: AsteroidsDatabase) {

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()){
            it.asDomainModel()
        }

    val pictureOfDay = Transformations.map(database.asteroidDao.getPictureOfDay()){
        it?.asDomainModel()
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroidsString = Network.asteroids.getAsteroidsList(
                    getNextSevenDaysFormattedDates()[0],
                    getNextSevenDaysFormattedDates()[getNextSevenDaysFormattedDates().size - 2],
                    Constants.API_KEY
                )
                val json = JSONObject(asteroidsString)
                val asteroidsList = parseAsteroidsJsonResult(json)
                database.asteroidDao.insertAll(*NetworkAsteroidContainer(asteroidsList).asDatabaseModel())
            } catch (exc: Exception) {
                Log.e("RefreshAsteroids","Unable to refreshAsteroids: " + exc.message)
            }
        }
    }

    suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO) {
            try {
                val picture = Network.pictureOfDay.getPictureOfDay(Constants.API_KEY)
                database.asteroidDao.insertPictureOfDay(picture.asDatabaseModel())
            } catch (exc: Exception) {
                Log.e("RefreshPicture","Unable to refreshPictureOfDay: " + exc.message)
            }
        }
    }



}