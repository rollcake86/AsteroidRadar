package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDatabaseDao {

    @Insert
    fun insert(asteroid : Asteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: Asteroid)

    @Update
    fun update(asteroid: Asteroid)

    @Delete
    fun delete(asteroid: Asteroid)

    @Query("SELECT * FROM asteroid_table")
    fun getAsteroidList() : LiveData<List<Asteroid>>

}