package com.igmata.lookout.ui.element

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.igmata.lookout.util.Connection

@Composable
fun EguneraketaDenboraOption() {
    var sliderPosition by remember { mutableFloatStateOf(Connection.SendingInterval.toFloat()) }

    Text(
        text = "Eguneraketa denbora: " + (sliderPosition.toInt() / 1000).toString() + "s",
        fontSize = 20.sp,
        modifier = Modifier
            .padding(0.dp, 30.dp, 0.dp, 0.dp)
    )
    Slider(
        value = sliderPosition,
        onValueChange = {
            sliderPosition = it
            Connection.SendingInterval = it.toLong()
        },
        valueRange = 5000f..60000f,
        steps = 10
    )
}