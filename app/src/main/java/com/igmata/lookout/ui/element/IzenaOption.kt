package com.igmata.lookout.ui.element

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.igmata.lookout.ui.activity.MainActivity
import com.igmata.lookout.util.Connection

@Composable
fun IzenaOption(viewModel: MainActivity.MainViewModel, context: MainActivity) {
    var izena by remember { mutableStateOf(Connection.Izena) }

    Text(
        text = "Izena",
        fontSize = 20.sp,
        modifier = Modifier
            .padding(0.dp, 30.dp, 0.dp, 0.dp)
    )
    TextField(
        value = izena,
        onValueChange = {
            if (it.length <= 15)
                izena = it
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
    Button(
        onClick = {
            viewModel.setIzena(izena, context)
        },
        modifier = Modifier.fillMaxWidth()
    ) { Text(text = "Gorde") }
}