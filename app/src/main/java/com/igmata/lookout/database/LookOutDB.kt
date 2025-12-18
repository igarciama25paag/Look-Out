package com.igmata.lookout.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.igmata.lookout.database.dao.Contacts
import com.igmata.lookout.database.dao.Locations
import com.igmata.lookout.database.entity.Contact
import com.igmata.lookout.database.entity.Location

@Database(entities = [Contact::class, Location::class], version = 1, exportSchema = false)
abstract class LookOutDB : RoomDatabase() {
    abstract fun contacts(): Contacts
    abstract fun locations(): Locations

    companion object {
        @Volatile
        private var INSTANCE: LookOutDB? = null

        fun getInstance(context: Context): LookOutDB =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    LookOutDB::class.java,
                    "LookOutDB"
                ).build().also { INSTANCE = it }
            }
    }
}