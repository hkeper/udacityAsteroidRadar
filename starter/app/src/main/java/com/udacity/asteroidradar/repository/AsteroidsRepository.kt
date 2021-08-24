package com.udacity.asteroidradar.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidsRepository () {

    /**
     * A playlist of videos that can be shown on the screen.
     */
//    val videos: LiveData<List<Asteroid>> =
//        Transformations.map(database.videoDao.getVideos()) {
//            it.asDomainModel()
//        }

    /**
     * Refresh the videos stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     * To actually load the videos for use, observe [videos]
     */
    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
//            val asteroidsList = Network.asteroids.getAsteroidsListAsync()



//            Log.i("!!!!!", asteroidsList.get(0).codename)
            //database.videoDao.insertAll(*playlist.asDatabaseModel())
        }
    }
}