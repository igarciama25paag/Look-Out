package com.igmata.lookout.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.igmata.lookout.R
import com.igmata.lookout.ui.activity.MainActivity
import com.igmata.lookout.ui.activity.MainActivity.MainViewModel
import com.igmata.lookout.ui.element.ConnectionButton
import com.igmata.lookout.ui.element.LocationElement
import com.igmata.lookout.ui.theme.Black
import com.igmata.lookout.ui.theme.White
import com.igmata.lookout.util.Connection.Companion.Connected

@Composable
fun MapPage(viewModel: MainViewModel, context: MainActivity) {
    val windowConf = LocalConfiguration.current
    val screenHorCenter = windowConf.screenWidthDp / 2
    val screenVerCenter = windowConf.screenHeightDp / 2

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            viewModel.locations.value.forEach {
                LocationElement(
                    it,
                    screenHorCenter,
                    screenVerCenter
                )
            }
        }
        ConnectionButton(viewModel, context)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
            )
            Text(
                text = "Zu",
                color = White
            )
        }
    }
}