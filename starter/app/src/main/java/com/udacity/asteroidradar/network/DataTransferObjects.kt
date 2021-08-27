package com.udacity.asteroidradar.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.DatabasePicture
import com.udacity.asteroidradar.domain.Asteroid

@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val asteroids: List<Asteroid>)

data class NetworkPictureOfDay(
    @Json(name = "media_type")
    val mediaType: String,
    val url: String,
    val title: String
)

fun NetworkPictureOfDay.asDatabaseModel(): DatabasePicture {
    return DatabasePicture(
        url = this.url,
        mediaType = this.mediaType,
        title = this.title
    )
}

fun NetworkAsteroidContainer.asDatabaseModel(): Array<DatabaseAsteroid> {
    return asteroids.map {
        DatabaseAsteroid (
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous)
    }.toTypedArray()
}
