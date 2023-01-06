package com.udacity.asteroidradar.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.asDomainModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.NasaApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.json.JSONObject

class NasaRepository(private val database: AsteroidDatabase) {

    val asteroids : LiveData<List<Asteroid>> = Transformations.map(database.asteroidDatabaseDao.getAsteroidList()) {
        it.asDomainModel()
    }


    suspend fun getAsteroidFromNasa(nasaApiService: NasaApiService): ArrayList<Asteroid> {
        val request = nasaApiService.getAsteroid()
        val result = coroutineScope {
            val deferred = async {
                 parseAsteroidsJsonResult(JSONObject(request.await()))
            }
            deferred.await()
        }
        return result
    }

    suspend fun getImageOfDay(nasaApiService: NasaApiService): PictureOfDay {
        val request = nasaApiService.getImageOfDay()
        val result = coroutineScope {
            val deferred = async {
                request.await()
            }
            deferred.await()
        }
        return result
    }
}