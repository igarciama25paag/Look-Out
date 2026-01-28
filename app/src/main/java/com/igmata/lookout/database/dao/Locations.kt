package com.igmata.lookout.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.igmata.lookout.database.entity.Location

@Dao
interface Locations {

    @Query("SELECT * FROM Location")
    fun getAll(): List<Location>

    @Query("DELETE FROM Location")
    fun deleteAll()

    @Insert
    fun insert(vararg location: Location)

    @Delete
    fun delete(vararg location: Location)

    @Update
    fun update(vararg location: Location)

    @Upsert
    fun upsert(vararg location: Location)

}