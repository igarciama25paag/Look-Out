package com.igmata.lookout.ui.element

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.igmata.lookout.R
import com.igmata.lookout.ui.theme.White

const val SIZE = 40
@Composable
fun LocationElement(
    item: LocationItem
) {
    val hc = (item.parentW - SIZE) / 2
    val vc = (item.parentH - SIZE) / 2

    val x = remember { mutableIntStateOf((hc + item.location.longitudea).toInt()) }
    val y = remember { mutableIntStateOf((vc + item.location.latitudea).toInt()) }

    Image(
        painter = painterResource(R.drawable.ic_location),
        contentDescription = "ic_location",
        modifier = Modifier
            .offset(x.intValue.dp,y.intValue.dp)
            .size(SIZE.dp)
            .clip(CircleShape)
            .background(White)
    )
}