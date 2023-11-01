package info.bati11.android.otameshi.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import info.bati11.android.otameshi.OtameshiFeature
import info.bati11.android.otameshi.common.ui.theme.OtameshiAppTheme

@Preview
@Composable
fun PreviewFeatureListScreen() {
    OtameshiAppTheme {
        Surface {
            FeatureListScreen()
        }
    }
}

@Composable
fun FeatureListScreen(
    features: List<OtameshiFeature> = emptyList(),
    onSelectedFeature: (OtameshiFeature) -> Unit = {},
) {
    LazyColumn {
        items(items = features) { feature ->
            val idx = features.indexOf(feature)
            FeatureRow(
                feature.label,
                Modifier
                    .background(
                        color = if (idx % 2 == 0) Color(0xFFC4EED0) else Color.White,
                    )
                    .clickable(
                        enabled = true,
                        onClick = { onSelectedFeature(feature) },
                    ),
            )
        }
    }
}

@Composable
fun FeatureRow(feature: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(text = feature, modifier = Modifier.padding(16.dp))
    }
}
