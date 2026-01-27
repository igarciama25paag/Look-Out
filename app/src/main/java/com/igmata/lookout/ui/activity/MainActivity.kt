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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igmata.lookout.R
import com.igmata.lookout.database.LookOutDB
import com.igmata.lookout.database.entity.Location
import com.igmata.lookout.ui.element.LocationItem
import com.igmata.lookout.ui.theme.Black
import com.igmata.lookout.ui.theme.LookOutTheme
import com.igmata.lookout.ui.theme.White
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MainContent()
        }
    }
}

class MainViewModel(context: Context): ViewModel() {
    val db by lazy { LookOutDB.getInstance(context) }
    val locationsDao by lazy { db.locations() }
    var connected = false

    fun getLocations(): List<Location> {
        val locations = mutableListOf<Location>()
        viewModelScope.launch(Dispatchers.IO) {
            //locationsDao.insert(Location(0,0,0.0, 0.0))
            locations.addAll(locationsDao.getAll())
        }
        return locations
    }
}

@Composable
fun MainContent() {
    val context = LocalContext.current
    val viewModel = MainViewModel(context)

    LookOutTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
        ) {
            MainMenu()
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                /*viewModel.getLocations().forEach {
                    LocationElement(item = it)
                }*/
            }
        }
    }
}

@Composable
fun MainMenu() {
    Image(
        painter = painterResource(R.drawable.ic_menu_button),
        contentDescription = "ic_menu_button",
        modifier = Modifier
            .offset(20.dp, 20.dp)
            .size(60.dp)
            .clip(CircleShape)
            .background(White)
    )
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
            Location("1","Zu",0.0, 0.0),
            width,
            height
        )
    )
    MainContent()
}