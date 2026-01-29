package com.igmata.lookout.ui.element

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
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

const val SIZE = 40
@Composable
fun LocationElement(
    location: Location,
    parentHorCenter: Int,
    parentVerCenter: Int
) {
    val x = (parentHorCenter - SIZE + location.longitudea - Connection.Longitudea).toInt()
    val y = (parentVerCenter - SIZE + location.latitudea - Connection.Latitudea).toInt()

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