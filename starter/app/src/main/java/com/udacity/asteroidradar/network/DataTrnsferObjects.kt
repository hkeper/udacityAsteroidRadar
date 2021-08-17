package com.udacity.asteroidradar.network

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.domain.Asteroid

/**
 * AsteroidHolder holds a list of Asteroids.
 *
 * This is to parse first level of our network result which looks like
 *
 * {
 *   "Asteroids": []
 * }
 */
@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val Asteroids: List<NetworkAsteroid>)

/**
 * Asteroids represent a devbyte that can be played.
 */
@JsonClass(generateAdapter = true)
data class NetworkAsteroid(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean)

/**
 * Convert Network results to database objects
 */
fun NetworkAsteroidContainer.asDomainModel(): List<Asteroid> {
    return Asteroids.map {
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
    return Asteroids.map {
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
