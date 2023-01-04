package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDatabaseDao {

    @Insert
    fun insert(asteroid : Asteroid)

    @Update
    fun update(asteroid: Asteroid)

    @Delete
    fun delete(asteroid: Asteroid)

    @Query("SELECT * FROM asteroid_table")
    fun getAsteroidList() : LiveData<List<Asteroid>>

}