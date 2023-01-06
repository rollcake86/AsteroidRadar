package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(asteroid : Asteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: Asteroid)

    @Update
    fun update(asteroid: Asteroid)

    @Delete
    fun delete(asteroid: Asteroid)

    @Query("select * from asteroid_table where closeApproachDate >= date('now')")
    fun getAsteroidList() : LiveData<List<Asteroid>>

}