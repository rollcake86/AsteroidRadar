package com.udacity.asteroidradar.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.API_QUERY_DATE_FORMAT
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.asDomainModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.NasaApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NasaRepository(private val database: AsteroidDatabase) {

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDatabaseDao.getAsteroidList()) {
            it.asDomainModel()
        }


    suspend fun getAsteroidFromNasa(nasaApiService: NasaApiService): ArrayList<Asteroid> {
        val request = nasaApiService.getAsteroidAsync(
            startTime = getTime(0),
            endTime = getTime(5),
            apiKey = API_KEY
        )
        val result = coroutineScope {
            val deferred = async {
                parseAsteroidsJsonResult(JSONObject(request.await()))
            }
            deferred.await()
        }
        return result
    }

    private fun getTime(days: Int): String? {
        val cal = Calendar.getInstance()
        cal.time = Date()
        val df: DateFormat = SimpleDateFormat(API_QUERY_DATE_FORMAT)
        cal.add(Calendar.DAY_OF_MONTH, days)
        return df.format(cal.time)
    }

    suspend fun getImageOfDay(nasaApiService: NasaApiService): PictureOfDay {
        val request = nasaApiService.getImageOfDayAsync()
        val result = coroutineScope {
            val deferred = async {
                request.await()
            }
            deferred.await()
        }
        return result
    }
}