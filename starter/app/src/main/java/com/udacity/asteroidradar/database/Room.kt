package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Query("select * from databaseasteroid order by closeApproachDate asc")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("select * from databaseasteroid where date(closeApproachDate) between date('now') and date('now','+7 days') order by closeApproachDate asc")
    fun getWeekAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("select * from databaseasteroid where closeApproachDate = date('now') order by closeApproachDate asc")
    fun getTodayAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("delete from databaseasteroid where closeApproachDate <= date('now')")
    suspend fun deletePreviousAsteroids()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPictureOfDay(vararg pictureOfDay: DatabasePicture)

    @Query("select * from databasepicture")
    fun getPictureOfDay(): LiveData<DatabasePicture>

    @Query("delete from databasepicture")
    suspend fun deleteAllPictures()
}

@Database(entities = [DatabaseAsteroid::class, DatabasePicture::class],
    version = 2,
    exportSchema = false)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidsDatabase::class.java,
                "asteroids")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}
