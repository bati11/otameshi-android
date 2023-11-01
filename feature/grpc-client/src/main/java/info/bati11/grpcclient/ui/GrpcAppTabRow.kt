package info.bati11.grpcclient.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import info.bati11.grpcclient.GrpcFeatureDestination
import java.util.*

@Composable
internal fun GrpcAppTabRow(
    allScreens: List<GrpcFeatureDestination>,
    onTabSelected: (GrpcFeatureDestination) -> Unit,
    currentScreen: GrpcFeatureDestination,
) {
    Surface(
        Modifier.fillMaxWidth(),
    ) {
        Row(Modifier.selectableGroup()) {
            allScreens.forEach { screen ->
                GrpcAppTab(
                    text = screen.label,
                    onSelected = { onTabSelected(screen) },
                    selected = currentScreen == screen,
                )
            }
        }
    }
}

@Composable
private fun GrpcAppTab(
    text: String,
    onSelected: () -> Unit,
    selected: Boolean,
) {
    val color = MaterialTheme.colors.onSurface
    val durationMillis = if (selected) TabFadeInAnimationDuration else TabFadeOutAnimationDuration
    val animSpec = remember {
        tween<Color>(
            durationMillis = durationMillis,
            easing = LinearEasing,
            delayMillis = TabFadeInAnimationDelay,
        )
    }
    val tabTintColor by animateColorAsState(
        targetValue = if (selected) color else color.copy(alpha = 0.6f),
        animationSpec = animSpec,
    )
    Row(
        modifier = Modifier
            .padding(16.dp)
            .animateContentSize()
            .selectable(
                selected = selected,
                onClick = onSelected,
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = false,
                    radius = Dp.Unspecified,
                    color = Color.Unspecified,
                ),
            )
            .clearAndSetSemantics { contentDescription = text },
    ) {
        Text(text.uppercase(Locale.getDefault()), color = tabTintColor)
    }
}

private const val TabFadeInAnimationDuration = 150
private const val TabFadeInAnimationDelay = 100
private const val TabFadeOutAnimationDuration = 100
