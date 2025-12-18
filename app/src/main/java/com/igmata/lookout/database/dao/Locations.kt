package com.igmata.lookout.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.igmata.lookout.database.entity.Location

@Dao
interface Locations {

    @Query("SELECT * FROM Location")
    fun getAll(): List<Location>

    @Insert
    fun insert(vararg location: Location)
}