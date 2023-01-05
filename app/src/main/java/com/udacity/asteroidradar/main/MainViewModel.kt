package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
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
    private val respository = NasaRepository(database)
    private var currentJob: Job? = null

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
                    val asteroidList =  respository.getAsteroidFromNasa(NasaApi.retrofitService)
                    Timber.i("${asteroidList.size}")
                    database.asteroidDatabaseDao.insertAll(*asteroidList.toTypedArray())
                    _asteroidList.postValue(asteroidList)
                } catch (e: IOException) {
                    _asteroidList.value = listOf()
                }
            }
        }
    }
}