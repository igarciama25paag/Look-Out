package com.igmata.lookout.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact(
    @PrimaryKey val id: Int,
    val izena: String,
    val connected: Boolean
)