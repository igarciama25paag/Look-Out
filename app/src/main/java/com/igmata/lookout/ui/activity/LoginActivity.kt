package com.igmata.lookout.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.igmata.lookout.ui.activity.ui.theme.LookOutTheme
import com.igmata.lookout.util.connection.Connection

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LookOutTheme {
                LoginContent()
            }
        }
    }
}

class LoginViewModel(context: Context): ViewModel() {
    var izena = mutableStateOf("")
}

@Composable
fun LoginContent() {
    val context = LocalContext.current
    val viewModel = LoginViewModel(context)
    var izena by remember { viewModel.izena }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 0.dp, 0.dp, 100.dp)
    ) {
        Text(
            text = "Izena sartu",
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(10.dp)
        )
        TextField(
            value = izena,
            onValueChange = { if (it.length <= 15) izena = it },
            singleLine = true
        )
        Button(
            onClick = {
                if (izena.isNotEmpty()) {
                    Connection.newConnection(izena)
                    context.startActivity(Intent(context, MainActivity::class.java))
                } else
                    Toast.makeText(context, "Izen bat sartu behar da", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .padding(7.dp)
                .size(250.dp, 45.dp)
        ) {
            Text(text = "Konektatu", fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LookOutTheme {
        LoginContent()
    }
}