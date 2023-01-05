package com.udacity.asteroidradar.search

import android.util.Log
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.NasaApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.json.JSONObject

class NasaRepository(private val database: AsteroidDatabase) {

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

}