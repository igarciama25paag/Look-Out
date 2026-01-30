package com.igmata.lookout.ui.element

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.igmata.lookout.R
import com.igmata.lookout.database.entity.Location
import com.igmata.lookout.ui.theme.White
import com.igmata.lookout.util.Connection
import kotlin.math.cos

const val SIZE = 40

@Composable
fun LocationElement(
    location: Location,
    parentHorCenter: Int,
    parentVerCenter: Int
) {
    // Longitude eta latitude diferentzia lortu
    val lon = location.longitudea - Connection.Longitudea
    val lat = location.latitudea - Connection.Latitudea

    //Log.d("LocationElement", "${location.name} lon: $lon, lat: $lat")

    // Longitudea eta latitudea metrota pasa
    val x = (parentHorCenter - SIZE / 2 + lon * 111320.0 / Connection.Eskala * cos(lat))
    val y = (parentVerCenter - SIZE - lat * 111320.0 / Connection.Eskala)

    //Log.d("LocationElement", "${location.name} x: $x, y: $y")

    // Elementuaren itxura
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .offset(x.dp, y.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_location),
            contentDescription = "ic_location",
            modifier = Modifier
                .size(SIZE.dp)
                .clip(CircleShape)
                .background(White)
        )
        Text(
            text = location.name,
            color = White,
            textAlign = TextAlign.Center
        )
    }
}