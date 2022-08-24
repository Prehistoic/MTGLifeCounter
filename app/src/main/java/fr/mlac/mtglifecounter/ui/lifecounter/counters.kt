package fr.mlac.mtglifecounter.ui.lifecounter

import android.text.BoringLayout
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay

@Composable
fun LifepointChangeCounter(
    lifepoints_change: Int,
    resetButtonIsPressed: Boolean
) {
    var gotRecentlyReset by remember {
        mutableStateOf(false)
    }

    // Protection against the reappearance of 0 after reset
    if (resetButtonIsPressed) gotRecentlyReset = true
    if (lifepoints_change != 0) gotRecentlyReset = false

    var valueToDisplay = lifepoints_change.toString()

    if (lifepoints_change > 0) {
        valueToDisplay = "+" + valueToDisplay
    }

    if (!resetButtonIsPressed && !gotRecentlyReset) {
        Text(
            text = valueToDisplay,
            style = MaterialTheme.typography.caption
        )
    }

}

@Composable
fun LifepointCounter(
    lifepoints: String,
    setResetButtonIsPressed: (Boolean) -> Unit,
    resetButtonIsPressed: Boolean
) {

    val rotation by animateFloatAsState(
        targetValue = if (resetButtonIsPressed) 180f else 0f,
        animationSpec = tween(500)
    )
    Text(
        text = lifepoints,
        style = MaterialTheme.typography.body1.copy(
            shadow = Shadow(
                color = MaterialTheme.colors.primaryVariant,
                offset = Offset(4f, 4f),
                blurRadius = 8f
            )
        ),
        modifier = Modifier
            .graphicsLayer {
                rotationX = rotation
            }
            .then(if (rotation > 90f) Modifier.scale(scaleX = 1f, scaleY = -1f) else Modifier)
    )

    LaunchedEffect(rotation) {
        delay(500)
        setResetButtonIsPressed(false)
    }
}