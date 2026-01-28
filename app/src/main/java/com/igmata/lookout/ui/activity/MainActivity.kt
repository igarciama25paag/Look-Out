package com.igmata.lookout.ui.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.igmata.lookout.R
import com.igmata.lookout.database.entity.Location
import com.igmata.lookout.ui.element.LocationItem
import com.igmata.lookout.ui.theme.Black
import com.igmata.lookout.ui.theme.LookOutTheme
import com.igmata.lookout.ui.theme.White
import com.igmata.lookout.util.Connection

class MainActivity : ComponentActivity() {
    val connection by lazy { Connection(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainContent()
        }
    }

    class MainViewModel(context: Context) : ViewModel() {

    }

    @Composable
    fun MainContent() {
        val context = LocalContext.current
        //val viewModel = MainViewModel(context)

        LookOutTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Black)
            ) {
                ConnectionButton()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                }
            }
        }
    }

    @Composable
    fun ConnectionButton() {
        val isConnected = remember { mutableStateOf(Connection.Connected) }
        IconButton(
            onClick = {
                if (Connection.Connected) {
                    connection.disconnect()
                    isConnected.value = false
                } else {
                    connection.connect(Connection.Izena.toString())
                    isConnected.value = true
                }
            },
            modifier = Modifier
                .offset(20.dp, 20.dp)
                .size(60.dp)
        ) {
            Icon(
                painter = painterResource(
                    if (isConnected.value) R.drawable.ic_connected
                    else R.drawable.ic_disconnected
                ),
                contentDescription = "ic_menu_button",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(60.dp)
                    .background(White)
                    .padding(5.dp)
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun Preview() {
        val config = LocalConfiguration.current
        val width = config.screenWidthDp
        val height = config.screenHeightDp
        val items = mutableListOf<LocationItem>()

        items.add(
            LocationItem(
                Location("1", "Zu", 0.0, 0.0),
                width,
                height
            )
        )
        MainContent()
    }
}