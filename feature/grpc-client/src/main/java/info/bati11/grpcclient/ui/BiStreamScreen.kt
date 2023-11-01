package info.bati11.grpcclient.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import info.bati11.grpcclient.gateway.GreetingService
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@Composable
internal fun BiStreamScreen(
    names: List<String>,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    val responseMessage = GreetingService.biStream.collectAsState(initial = "")
    var connectFailMessage = GreetingService.biStreamError.collectAsState(initial = "")

    var index by remember { mutableStateOf(0) }
    var messages by remember { mutableStateOf(listOf<String>()) }
    LaunchedEffect(true) {
        GreetingService.connectBiStream()
    }
    if (connectFailMessage.value != "") {
        Text("Failed to connect: ${connectFailMessage.value}")
    } else {
        Column() {
            Text("Success to connect.")
            LazyColumn {
                items(messages) { Text(text = it) }
            }
            Button(
                onClick = {
                    val name = names[index]
                    messages = messages + "$name ->"
                    index = if (index + 1 == names.size) 0 else index + 1
                    coroutineScope.launch {
                        GreetingService.helloBiDirection(name)
                    }
                },
            ) {
                Text("button")
            }
            Text(text = responseMessage.value)
        }
    }
}
