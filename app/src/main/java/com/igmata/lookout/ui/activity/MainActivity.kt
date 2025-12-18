package com.igmata.lookout.ui.activity

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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.igmata.lookout.R
import com.igmata.lookout.database.LookOutDB
import com.igmata.lookout.database.entity.Location
import com.igmata.lookout.ui.element.LocationElement
import com.igmata.lookout.ui.element.LocationItem
import com.igmata.lookout.ui.theme.Black
import com.igmata.lookout.ui.theme.LookOutTheme
import com.igmata.lookout.ui.theme.White
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    val db by lazy { LookOutDB.getInstance(this) }
    val locationsDao by lazy { db.locations() }
    val contactsDao by lazy { db.contacts() }

    val locations = mutableStateListOf<Location>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch(Dispatchers.IO) {
            //locationsDao.insert(Location(0,0,0.0, 0.0))
            locations.addAll(locationsDao.getAll())
        }

        val locationItems = mutableStateListOf<LocationItem>()

        setContent {
            val config = LocalConfiguration.current
            val width = config.screenWidthDp
            val height = config.screenHeightDp

            locations.forEach {
                locationItems.add(LocationItem(it, width, height))
            }
            TheContent(locationItems)
        }

        Thread {
            while (true) {
                locationItems.forEach {
                    it.location.longitudea += 10
                }
                Thread.sleep(1000)
            }
        }.start()
    }
}

@Composable
fun TheContent(items: List<LocationItem>) {
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
                items.forEach {
                    LocationElement(item = it)
                }
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
            Location(1,1,0.0, 0.0),
            width,
            height
        )
    )
    TheContent(items)
}