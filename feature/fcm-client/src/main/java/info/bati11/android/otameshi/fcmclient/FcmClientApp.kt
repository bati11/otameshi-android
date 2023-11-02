package info.bati11.android.otameshi.fcmclient

import androidx.compose.runtime.Composable
import info.bati11.android.otameshi.common.ui.theme.OtameshiAppTheme
import info.bati11.android.otameshi.fcmclient.ui.FcmClientScreen

@Composable
fun FcmClientApp() {
    OtameshiAppTheme {
        FcmClientScreen()
    }
}
