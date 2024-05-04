package fr.mlac.mtglifecounter.ui.lifecounter

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay

@Composable
fun LifepointChangeCounter(
    modifier: Modifier = Modifier,
    lifepointsChange: Int,
    resetButtonIsPressed: Boolean,
    newStartingLifepointsButtonIsPressed: Boolean
) {
    var gotRecentlyReset by remember {
        mutableStateOf(false)
    }

    // Protection against the reappearance of 0 after reset
    if (resetButtonIsPressed || newStartingLifepointsButtonIsPressed) gotRecentlyReset = true
    if (lifepointsChange != 0) gotRecentlyReset = false

    var valueToDisplay = lifepointsChange.toString()

    if (lifepointsChange > 0) {
        valueToDisplay = "+$valueToDisplay"
    }

    if (!resetButtonIsPressed && !newStartingLifepointsButtonIsPressed && !gotRecentlyReset) {
        Text(
            text = valueToDisplay,
            style = MaterialTheme.typography.caption,
            modifier = modifier
        )
    }

}

@Composable
fun LifepointCounter(
    modifier: Modifier = Modifier,
    lifepoints: String,
    setResetButtonIsPressed: (Boolean) -> Unit,
    setNewStartingLifepointsButtonIsPressed: (Boolean) -> Unit,
    resetButtonIsPressed: Boolean,
    newStartingLifepointsButtonIsPressed: Boolean
) {
    val rotation by animateFloatAsState(
        targetValue = if (resetButtonIsPressed || newStartingLifepointsButtonIsPressed) 180f else 0f,
        animationSpec = tween(500)
    )
    Text(
        text = lifepoints,
        style = MaterialTheme.typography.h1.copy(
            shadow = Shadow(
                color = MaterialTheme.colors.primaryVariant,
                offset = Offset(4f, 4f),
                blurRadius = 8f
            )
        ),
        modifier = modifier
            .graphicsLayer {
                rotationX = rotation
            }
            .then(if (rotation > 90f) Modifier.scale(scaleX = 1f, scaleY = -1f) else Modifier)
    )

    LaunchedEffect(rotation) {
        delay(500)
        setResetButtonIsPressed(false)
        setNewStartingLifepointsButtonIsPressed(false)
    }
}