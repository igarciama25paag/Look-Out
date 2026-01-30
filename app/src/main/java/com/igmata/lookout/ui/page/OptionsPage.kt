package com.igmata.lookout.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.igmata.lookout.ui.activity.MainActivity
import com.igmata.lookout.ui.element.EguneraketaDenboraOption
import com.igmata.lookout.ui.element.EskalaOption
import com.igmata.lookout.ui.element.IzenaOption

@Composable
fun OptionsPage(viewModel: MainActivity.MainViewModel, context: MainActivity) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        Text(
            text = "Aukerak",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            modifier = Modifier.fillMaxWidth()
        )

        IzenaOption(viewModel, context)
        EskalaOption()
        EguneraketaDenboraOption()
    }
}