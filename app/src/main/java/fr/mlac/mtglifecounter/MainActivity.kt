package fr.mlac.mtglifecounter

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    var player1 = Player(20)
    var player2 = Player(20)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KeepScreenOn()

            Column(modifier = Modifier
                .fillMaxSize()
            ) {
                PlayerBox(player1, R.drawable.blurred_blue)
                PlayerBox(player2, R.drawable.blurred_fire)
            }
        }
    }
}

@Composable
fun KeepScreenOn() = AndroidView({ View(it).apply { keepScreenOn = true } })

@Composable
fun ColumnScope.PlayerBox(player: Player, background: Int) {

    val lastChangeIsVisible = remember { MutableTransitionState(false)}
    val plusButtonClicked = remember { MutableInteractionSource() }
    val minusButtonClicked = remember { MutableInteractionSource() }
    var plusButtonPressed by remember { mutableStateOf(false)}
    var minusButtonPressed by remember { mutableStateOf(false)}

    if (!lastChangeIsVisible.currentState && lastChangeIsVisible.isIdle) {
        player.resetLastChange()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
    ) {
        Image(
            painter = painterResource(id = background),
            contentDescription = "background",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds
        )
        Box(
            modifier = Modifier.matchParentSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-1).dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AnimatedVisibility(
                    visibleState = lastChangeIsVisible,
                    enter = EnterTransition.None,
                    exit = fadeOut(
                        animationSpec = tween(delayMillis = 2500, easing = LinearOutSlowInEasing)
                    )
                ) {
                    LifepointChangeCounter(player.lifepoints.last_change)
                }
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RepeatingButton(
                    onClick = { player.decreaseLifepoints() },
                    interactionSource = minusButtonClicked
                ) {
                    if (!minusButtonPressed) {
                        Icon(Icons.Outlined.RemoveCircleOutline, "minus", modifier = Modifier.size(50.dp), tint = Color.White)
                    } else {
                        Icon(Icons.Filled.RemoveCircle, "minus", modifier = Modifier.size(50.dp), tint = Color.White)
                    }
                }
                LifepointCounter(player.lifepoints.current.toString())
                RepeatingButton(
                    onClick = { player.increaseLifepoints() },
                    interactionSource = plusButtonClicked
                ) {
                    if (!plusButtonPressed) {
                        Icon(Icons.Outlined.AddCircleOutline, "plus", modifier = Modifier.size(50.dp), tint = Color.White)
                    } else {
                        Icon(Icons.Filled.AddCircle, "plus", modifier = Modifier.size(50.dp), tint = Color.White)
                    }

                }

                LaunchedEffect(plusButtonClicked) {
                    plusButtonClicked.interactions.collect { interaction ->
                        when (interaction) {
                            is PressInteraction.Release -> {
                                plusButtonPressed = false
                                lastChangeIsVisible.targetState = false
                            }
                            is PressInteraction.Press -> {
                                lastChangeIsVisible.targetState = true
                                plusButtonPressed = true
                            }
                        }
                    }
                }
                LaunchedEffect(minusButtonClicked) {
                    minusButtonClicked.interactions.collect { interaction ->
                        when (interaction) {
                            is PressInteraction.Release -> {
                                minusButtonPressed = false
                                lastChangeIsVisible.targetState = false
                            }
                            is PressInteraction.Press -> {
                                lastChangeIsVisible.targetState = true
                                minusButtonPressed = true
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun LifepointChangeCounter(lifepoints_change: Int) {

    var valueToDisplay = lifepoints_change.toString()
    if (lifepoints_change > 0) {
        valueToDisplay = "+" + valueToDisplay
    }

    Text(
        text = valueToDisplay,
        style = MaterialTheme.typography.h4.copy(
            color = Color.White,
            fontFamily= FontFamily.SansSerif,
            fontSize = 60.sp,
        )
    )
}

@Composable
fun LifepointCounter(lifepoints: String) {
    val fonts = FontFamily(
        Font(R.font.fester_regular)
    )

    Text(
        text = lifepoints,
        style = MaterialTheme.typography.h4.copy(
            color = Color.White,
            fontFamily= fonts,
            fontSize = 180.sp,
            shadow = Shadow(
                color = Color.Black,
                offset = Offset(4f, 4f),
                blurRadius = 8f
            )
        )
    )
}

@Composable
fun RepeatingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    maxDelayMillis: Long = 500,
    minDelayMillis: Long = 100,
    delayDecayFactor: Float = .15f,
    content: @Composable () -> Unit
) {

    val currentClickListener by rememberUpdatedState(onClick)

    IconButton(
        modifier = modifier
            .pointerInput(interactionSource, enabled) {
                forEachGesture {
                    coroutineScope {
                        awaitPointerEventScope {
                            val down = awaitFirstDown(requireUnconsumed = false)

                            val heldButtonJob = launch {
                                var currentDelayMillis = maxDelayMillis
                                while (enabled && down.pressed) {
                                    currentClickListener()
                                    delay(currentDelayMillis)
                                    val nextDelayMillis =
                                        currentDelayMillis - (currentDelayMillis * delayDecayFactor)
                                    currentDelayMillis =
                                        nextDelayMillis
                                            .toLong()
                                            .coerceAtLeast(minDelayMillis)
                                }
                            }

                            waitForUpOrCancellation()
                            heldButtonJob.cancel()
                        }
                    }
                }
            }
            .fillMaxHeight(),
        onClick = {},
        enabled = enabled,
        interactionSource = interactionSource,
        content = content
    )
}