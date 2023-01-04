package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.network.NasaApi
import com.udacity.asteroidradar.search.NasaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel : ViewModel() {

    private val respository = NasaRepository(NasaApi.retrofitService)
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
            try {
//                _asteroidList.value =
//                repository.getFilters().let {
//                    // only update the filters list if it's changed since the last time
//                    if (it != _regionList.value) {
//                        _regionList.value = it
//                    }
//                }
                respository.getAsteroidFromNasa()
            } catch (e: IOException) {
                _asteroidList.value = listOf()
            }
        }
    }
}