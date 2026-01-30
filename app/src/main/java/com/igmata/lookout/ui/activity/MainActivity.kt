package com.igmata.lookout.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.igmata.lookout.database.LookOutDB
import com.igmata.lookout.database.entity.Location
import com.igmata.lookout.ui.page.MapPage
import com.igmata.lookout.ui.page.OptionsPage
import com.igmata.lookout.ui.theme.LookOutTheme
import com.igmata.lookout.util.Connection
import com.igmata.lookout.util.Connection.Companion.Connected
import kotlinx.coroutines.CoroutineScope
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

    // MainActivity-ko funtzioak
    class MainViewModel(context: MainActivity) : ViewModel() {
        val connection by lazy { Connection(context) }
        var locations = mutableStateOf(listOf<Location>())
        private val db by lazy { LookOutDB.Companion.getInstance(context) }
        private val locationsDao by lazy { db.locations() }

        fun setupLocationsRetriever(context: MainActivity) {
            CoroutineScope(Dispatchers.IO).launch {
                context.lifecycleScope.launch(Dispatchers.IO) {
                    locationsDao.deleteAll()
                }
                while (Connected) {
                    context.lifecycleScope.launch(Dispatchers.IO) {
                        locations.value = locationsDao.getAll()
                    }
                    Thread.sleep(5000)
                }
            }
        }

        fun connectionSwapper(viewModel: MainViewModel, context: MainActivity, isConnected: MutableState<Boolean>) {
            if (Connected) {
                viewModel.connection.disconnect()
                isConnected.value = false
                Toast.makeText(context, "Deskonektatuta", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.connection.connect(Connection.Izena)
                viewModel.setupLocationsRetriever(context)
                isConnected.value = true
                Toast.makeText(context, "Konektatuta", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        fun setIzena(izena: String, context: MainActivity) {
            if (!izena.isEmpty())
                Connection.Izena = izena
            else
                Toast.makeText(context, "Izen bat sartu behar da", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun Preview() {
        MainContent()
    }

    @Composable
    fun MainContent() {
        val context = this
        val viewModel = MainViewModel(context)
        viewModel.setupLocationsRetriever(context)

        val pagerState = rememberPagerState(0, 0f) { 2 }
        LookOutTheme {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> MapPage(viewModel, context)
                    1 -> OptionsPage(viewModel, context)
                }
            }
        }
    }
}