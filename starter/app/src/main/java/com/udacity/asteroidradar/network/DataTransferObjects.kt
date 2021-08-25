package com.udacity.asteroidradar.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.DatabasePicture
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay

@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val asteroids: List<Asteroid>)

//@JsonClass(generateAdapter = true)
//data class NetworkAsteroid(
//    val id: Long,
//    val codename: String,
//    val closeApproachDate: String,
//    val absoluteMagnitude: Double,
//    val estimatedDiameter: Double,
//    val relativeVelocity: Double,
//    val distanceFromEarth: Double,
//    val isPotentiallyHazardous: Boolean)

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

fun NetworkAsteroidContainer.asDomainModel(): List<Asteroid> {
    return asteroids.map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous)
    }
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
