package com.igmata.lookout.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.igmata.lookout.R
import com.igmata.lookout.database.LookOutDB
import com.igmata.lookout.database.entity.Location
import com.igmata.lookout.ui.element.LocationElement
import com.igmata.lookout.ui.theme.Black
import com.igmata.lookout.ui.theme.LookOutTheme
import com.igmata.lookout.ui.theme.White
import com.igmata.lookout.util.Connection
import com.igmata.lookout.util.Connection.Companion.Connected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val connection by lazy { Connection(this) }
    var locations = mutableStateOf(listOf<Location>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainContent()
        }
    }

    // MainActivity-ko funtzioak
    class MainViewModel(context: MainActivity) : ViewModel() {

        private val db by lazy { LookOutDB.Companion.getInstance(context) }
        private val locationsDao by lazy { db.locations() }

        fun setupLocationsRetriever(context: MainActivity) {
            CoroutineScope(Dispatchers.IO).launch {
                context.lifecycleScope.launch(Dispatchers.IO) {
                    locationsDao.deleteAll()
                }
                while (Connected) {
                    context.lifecycleScope.launch(Dispatchers.IO) {
                        context.locations.value = locationsDao.getAll()
                    }
                    Thread.sleep(5000)
                }
            }
        }
    }

    @Composable
    fun MainContent() {
        val viewModel = MainViewModel(this)
        viewModel.setupLocationsRetriever(this)
        val windowConf = LocalConfiguration.current
        val screenHorCenter = windowConf.screenWidthDp / 2
        val screenVerCenter = windowConf.screenHeightDp / 2

        LookOutTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Black)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    locations.value.forEach {
                        LocationElement(
                            it,
                            screenHorCenter,
                            screenVerCenter
                        )
                    }
                }
                ConnectionButton(viewModel)
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
    }

    @Composable
    fun ConnectionButton(viewModel: MainViewModel) {
        val isConnected = remember { mutableStateOf(Connected) }
        IconButton(
            onClick = {
                if (Connected) {
                    connection.disconnect()
                    isConnected.value = false
                    Toast.makeText(this, "Deskonektatuta", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    connection.connect(Connection.Izena)
                    viewModel.setupLocationsRetriever(this)
                    isConnected.value = true
                    Toast.makeText(this, "Konektatuta", Toast.LENGTH_SHORT)
                        .show()
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
        MainContent()
    }
}