package com.igmata.lookout.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Location(
    @PrimaryKey val id: String,
    val name: String,
    var longitudea: Double,
    var latitudea: Double
)