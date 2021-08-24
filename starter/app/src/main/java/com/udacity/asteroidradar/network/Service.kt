package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidsApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidsList(@Query("start_date") start_date: String,
                         @Query("end_date") end_date: String,
                         @Query("api_key") api_key: String): String
}

interface PictureOfDayApiService {
    @GET("planetary/apod")
    suspend fun getPictureOfDay(@Query("api_key") api_key: String): PictureOfDay
}

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object Network {
    private val retrofitAsteroids = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val retrofitPictureOfDay = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val asteroids : AsteroidsApiService by lazy{
        retrofitAsteroids.create(AsteroidsApiService::class.java)
    }

    val pictureOfDay : PictureOfDayApiService by lazy{
        retrofitPictureOfDay.create(PictureOfDayApiService::class.java)
    }
}
