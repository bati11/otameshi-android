package info.bati11.android.otameshi.fcmclient.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import info.bati11.android.otameshi.fcmclient.viewmodel.FcmClientViewModel

@Composable
internal fun FcmClientScreen(viewModel: FcmClientViewModel = viewModel()) {
    val applicationContext = LocalContext.current.applicationContext
    LaunchedEffect(true) {
        viewModel.initialize(applicationContext)
    }
    val fcmToken = viewModel.fcmToken.collectAsState()
    val message = viewModel.message.collectAsState()
    Column {
        Text(text = "FCM Token: ${fcmToken.value}")
        Text(text = "${message.value}")
    }
}
