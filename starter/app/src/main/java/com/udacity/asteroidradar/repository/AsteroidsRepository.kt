package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.network.NetworkAsteroidContainer
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

enum class AsteroidsFilter { SHOW_WEEK, SHOW_TODAY, SHOW_SAVED }

class AsteroidsRepository (private val database: AsteroidsDatabase) {

    val apiKey: String = BuildConfig.API_KEY

    fun getAsteroids(filter: AsteroidsFilter): LiveData<List<Asteroid>> {
        return when (filter) {
            AsteroidsFilter.SHOW_WEEK -> Transformations.map(database.asteroidDao.getWeekAsteroids()) {
                it.asDomainModel()
            }
            AsteroidsFilter.SHOW_TODAY -> Transformations.map(database.asteroidDao.getTodayAsteroids()) {
                it.asDomainModel()
            }
            AsteroidsFilter.SHOW_SAVED -> Transformations.map(database.asteroidDao.getAllAsteroids()) {
                it.asDomainModel()
            }
        }
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
                    apiKey
                )
                val json = JSONObject(asteroidsString)
                val asteroidsList = parseAsteroidsJsonResult(json)
                database.asteroidDao.insertAll(*NetworkAsteroidContainer(asteroidsList).asDatabaseModel())
            } catch (exc: Exception) {
                Timber.e("Unable to refreshAsteroids: %s", exc.message)
            }
        }
    }

    suspend fun deletePreviousAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                database.asteroidDao.deletePreviousAsteroids()
            } catch (exc: Exception) {
                Timber.e("Unable to deleteAsteroids: %s", exc.message)
            }
        }
    }

    suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO) {
            try {
                val picture = Network.pictureOfDay.getPictureOfDay(apiKey)
                database.asteroidDao.deleteAllPictures()
                database.asteroidDao.insertPictureOfDay(picture.asDatabaseModel())
            } catch (exc: Exception) {
                Timber.e("Unable to refreshPictureOfDay: %s", exc.message)
            }
        }
    }



}