package info.bati11.android.otameshi.awsiotclient

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val AWS_IOT_FEATURE_ROUTE = "feature/awsiot"

fun NavGraphBuilder.awsIotFeature() {
    composable(AWS_IOT_FEATURE_ROUTE) {
        AwsIotClientApp()
    }
}
