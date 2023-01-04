package com.udacity.asteroidradar.search

import android.util.Log
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.NasaApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.json.JSONObject

class NasaRepository(nasaApiService: NasaApiService) {

    private val request = nasaApiService.getAsteroid()

    suspend fun getAsteroidFromNasa(){
        val result = coroutineScope {
            val deferred = async {
                parseAsteroidsJsonResult(JSONObject(request.await()))
            }
        }
        return result
    }

}