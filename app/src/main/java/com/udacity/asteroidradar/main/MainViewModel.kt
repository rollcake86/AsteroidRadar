package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.NasaApi
import com.udacity.asteroidradar.search.NasaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidDatabase.getInstance(application)
    private val respository = NasaRepository(database)
    private var currentJob: Job? = null

    private val _todayImage = MutableLiveData<PictureOfDay>()

    val todayImage: LiveData<PictureOfDay>
        get() = _todayImage

//    private val _asteroidList = MutableLiveData<List<Asteroid>>()
//
//    val asteroidList: LiveData<List<Asteroid>>
//        get() = _asteroidList

    init {
        onQueryChanged()
    }
    var asteroidListForDB =  respository.asteroids
    private fun onQueryChanged() {
        currentJob?.cancel() // if a previous query is running cancel it before starting another
        currentJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val asteroidList = respository.getAsteroidFromNasa(NasaApi.retrofitService)
                    database.asteroidDatabaseDao.insertAll(*asteroidList.toTypedArray())
//                    _asteroidList.postValue(asteroidList)
                    asteroidListForDB =  respository.asteroids
                } catch (e: IOException) {
//                    _asteroidList.postValue(listOf())
                }
                try {
                    val dayOfImage = respository.getImageOfDay(NasaApi.retrofitService)
                    _todayImage.postValue(dayOfImage)
                } catch (e: IOException) {
//                    _todayImage.value = null
                }

            }
        }
    }
}