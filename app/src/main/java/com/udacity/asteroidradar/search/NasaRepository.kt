package com.udacity.asteroidradar.search

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.TodayImage
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.NasaApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.json.JSONObject

class NasaRepository() {

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

    suspend fun getImageOfDay(nasaApiService: NasaApiService): TodayImage {
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