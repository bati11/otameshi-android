package info.bati11.opengles.myopengles

import android.content.Intent
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import info.bati11.android.otameshi.common.ui.theme.OtameshiAppTheme
import info.bati11.opengles.myopengles.glapp.MyOpenGlEsActivity

@Composable
fun MyOpenGlEsApp() {
    OtameshiAppTheme {
        val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            backPressedDispatcher?.onBackPressed()
        }
        val context = LocalContext.current
        LaunchedEffect(true) {
            val intent = Intent(context, MyOpenGlEsActivity::class.java)
            launcher.launch(intent)
        }
    }
}
