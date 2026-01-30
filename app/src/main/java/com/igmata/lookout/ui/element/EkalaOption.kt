package com.igmata.lookout.ui.element

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.igmata.lookout.util.Connection
import kotlin.math.pow

@Composable
fun EskalaOption() {
    val values = listOf(1, 10, 100, 1000)
    var sliderPosition by remember { mutableIntStateOf(values.indexOf(Connection.Eskala)+1) }
    var eskala by remember { mutableIntStateOf(Connection.Eskala) }

    Text(
        text = "Eskala: " + eskala.toString() + "m",
        fontSize = 20.sp,
        modifier = Modifier
            .padding(0.dp, 30.dp, 0.dp, 0.dp)
    )
    Slider(
        value = sliderPosition.toFloat(),
        onValueChange = {
            sliderPosition = it.toInt()
            Connection.Eskala = values[sliderPosition-1]
            eskala = values[sliderPosition-1]
        },
        valueRange = 1f..4f,
        steps = 2
    )
}