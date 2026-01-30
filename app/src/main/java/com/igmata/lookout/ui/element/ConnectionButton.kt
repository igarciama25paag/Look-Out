package com.igmata.lookout.ui.element

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.igmata.lookout.R
import com.igmata.lookout.ui.activity.MainActivity
import com.igmata.lookout.ui.activity.MainActivity.MainViewModel
import com.igmata.lookout.ui.theme.White
import com.igmata.lookout.util.Connection.Companion.Connected

@Composable
fun ConnectionButton(viewModel: MainViewModel, context: MainActivity) {
    val isConnected = remember { mutableStateOf(Connected) }
    IconButton(
        onClick = {
            viewModel.connectionSwapper(viewModel, context, isConnected)
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