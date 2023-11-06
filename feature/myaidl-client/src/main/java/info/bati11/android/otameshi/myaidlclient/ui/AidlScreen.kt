package info.bati11.android.otameshi.myaidlclient.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import info.bati11.android.otameshi.myaidlclient.viewmodel.AidlViewModel

@Composable
fun AidlScreen(viewModel: AidlViewModel = viewModel()) {
    val context = LocalContext.current
    Column {
        Button(onClick = {
            viewModel.bindRemoteService(context)
        }) {
            Text("Bind Remote Service")
        }
        Button(onClick = {
            viewModel.unbindRemoteService(context)
        }) {
            Text("Unbind Remote Service")
        }
        Button(onClick = {
            viewModel.getPid()
        }) {
            Text("Get PID")
        }
    }
}
