package com.igmata.lookout.ui.element

import com.igmata.lookout.database.entity.Location

data class LocationItem(
    val location: Location,
    val parentW: Int,
    val parentH: Int
)