package com.igmata.lookout.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.igmata.lookout.R
import com.igmata.lookout.ui.activity.MainActivity
import com.igmata.lookout.ui.activity.MainActivity.MainViewModel
import com.igmata.lookout.ui.element.ConnectionButton
import com.igmata.lookout.ui.element.LocationElement
import com.igmata.lookout.ui.theme.Black
import com.igmata.lookout.ui.theme.Blue
import com.igmata.lookout.ui.theme.White
import com.igmata.lookout.util.Connection
import com.igmata.lookout.util.Connection.Companion.Connected

@Composable
fun MapPage(viewModel: MainViewModel, context: MainActivity) {

    // Leihoaren taimaina lortu
    val windowConf = LocalConfiguration.current
    val screenHorCenter = windowConf.screenWidthDp / 2
    val screenVerCenter = windowConf.screenHeightDp / 2

    // Maparen propietateak
    val properties by remember { mutableStateOf(MapProperties(mapType = MapType.TERRAIN)) }

    // Maparen interfaze aukerak
    val uiSettings by remember { mutableStateOf(MapUiSettings(
        zoomControlsEnabled = false,
        zoomGesturesEnabled = false,
        scrollGesturesEnabled = false,
        rotationGesturesEnabled = false,
        tiltGesturesEnabled = false,
        compassEnabled = false,
        mapToolbarEnabled = false
    )) }

    // Maparen posizio propietateak
    val cameraPositionState = rememberCameraPositionState()
    val latitudeState = remember { Connection.Latitudea }
    val longitudeState = remember { Connection.Longitudea }

    // Maparen posizioa eta zoom-a eguneratu
    LaunchedEffect(latitudeState.doubleValue, longitudeState.doubleValue) {
        val lat = latitudeState.doubleValue
        val lng = longitudeState.doubleValue
        val zoom = when (Connection.Eskala) {
            1 -> 17.3f
            10 -> 13.9f
            100 -> 10.6f
            else -> 7.3f
        }
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(lat, lng),
                zoom
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {
        // Mapa
        GoogleMap(
            properties = properties,
            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState
        )

        // Beste pertsonen kokalekua
        Box(modifier = Modifier.fillMaxSize()) {
            viewModel.locations.value.forEach {
                LocationElement(
                    it,
                    screenHorCenter,
                    screenVerCenter
                )
            }
        }

        // Konexio botoia
        ConnectionButton(viewModel, context)

        // Telefonoaren kokalekua
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
                    .background(Blue)
            )
            Text(
                text = "Zu",
                color = Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(White)
            )
        }
    }
}