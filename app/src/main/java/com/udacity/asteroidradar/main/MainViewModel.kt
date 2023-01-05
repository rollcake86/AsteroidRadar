package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.TodayImage
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.NasaApi
import com.udacity.asteroidradar.search.NasaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidDatabase.getInstance(application)
    private val respository = NasaRepository()
    private var currentJob: Job? = null

    private val _todayImage = MutableLiveData<TodayImage>()

    val todayImage: LiveData<TodayImage>
        get() = _todayImage

    private val _asteroidList = MutableLiveData<List<Asteroid>>()

    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    init {
        onQueryChanged()
    }

    private fun onQueryChanged() {
        currentJob?.cancel() // if a previous query is running cancel it before starting another
        currentJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val asteroidList = respository.getAsteroidFromNasa(NasaApi.retrofitService)
                    database.asteroidDatabaseDao.insertAll(*asteroidList.toTypedArray())
                    _asteroidList.postValue(asteroidList)
                } catch (e: IOException) {
                    _asteroidList.value = listOf()
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