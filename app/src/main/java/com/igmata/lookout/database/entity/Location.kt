package com.igmata.lookout.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Location(
    @PrimaryKey val id: Int,
    val contact: Int,
    var longitudea: Double,
    var latitudea: Double
)

data class ContactInLocation(
    val contact: Contact,
    @Relation(
        parentColumn = "id",
        entityColumn = "contact"
    )
    val locations: Location
)