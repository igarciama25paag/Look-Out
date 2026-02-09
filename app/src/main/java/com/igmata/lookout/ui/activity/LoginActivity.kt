package com.igmata.lookout.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.igmata.lookout.R
import com.igmata.lookout.ui.activity.ui.theme.LookOutTheme
import com.igmata.lookout.ui.theme.Black
import com.igmata.lookout.ui.theme.Blue
import com.igmata.lookout.ui.theme.White
import com.igmata.lookout.util.Connection

class LoginActivity : ComponentActivity() {
    val connection by lazy { Connection(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LookOutTheme {
                LoginContent()
            }
        }
    }

    // LoginActivity-ko funtzioak
    class LoginViewModel : ViewModel() {

        // Konektatzeko botiaren click
        fun conectClick(context: LoginActivity, izena: String) {
            if (izena.isNotEmpty()) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    context.connection.connect(izena)
                    context.startActivity(Intent(context, MainActivity::class.java))
                } else {
                    Toast.makeText(context, "Kokapen baimena behar da", Toast.LENGTH_SHORT)
                        .show()
                    ActivityCompat.requestPermissions(
                        context as ComponentActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        0
                    )
                }
            } else
                Toast.makeText(context, "Izen bat sartu behar da", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        LookOutTheme {
            LoginContent()
        }
    }

    @Composable
    fun LoginContent() {
        val context = LocalContext.current
        val viewModel = LoginViewModel()
        var izena by remember { mutableStateOf("") }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(R.drawable.background_img),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop
                )
                .padding(0.dp, 0.dp, 0.dp, 100.dp)
        ) {
            Text(
                text = "LOOK-OUT",
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                color = White,
                modifier = Modifier
                    .padding(0.dp,0.dp,0.dp,100.dp)
                    .clip(CircleShape)
                    .background(Blue)
                    .padding(20.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .border(5.dp, Blue, CircleShape)
                    .background(White, CircleShape)
                    .padding(30.dp)
            ) {
                Text(
                    text = "Izena sartu",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(10.dp)
                )
                TextField(
                    value = izena,
                    onValueChange = { if (it.length <= 15) izena = it },
                    singleLine = true
                )
                Button(
                    onClick = {
                        viewModel.conectClick(context as LoginActivity, izena)
                    },
                    modifier = Modifier
                        .padding(7.dp)
                        .size(250.dp, 45.dp)
                ) {
                    Text(text = "Konektatu", fontSize = 16.sp)
                }
            }
        }
    }
}