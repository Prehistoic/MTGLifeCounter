package fr.mlac.mtglifecounter

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
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
            MainScreen(player1, player2)
        }
    }
}

@Composable
fun KeepScreenOn() = AndroidView({ View(it).apply { keepScreenOn = true } })

@Composable
fun MainScreen(player1: Player, player2: Player) {

    var menuIsVisible by remember { mutableStateOf(false)}
    val yoffset : Float by animateFloatAsState(if (menuIsVisible) 1f else 0f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
    ) {
        MenuButton(
            modifier = Modifier
                .offset(0.dp, (-36 * yoffset).dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { menuIsVisible = !menuIsVisible }
        )

        MenuRow(menuIsVisible = menuIsVisible)

        Column(
            modifier = Modifier.matchParentSize()
        ) {
            PlayerBox(player1, R.drawable.blurred_blue)
            PlayerBox(player2, R.drawable.blurred_fire)
        }
    }
}

@Composable
fun BoxScope.MenuButton(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.center_logo),
        contentDescription = "logo",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .zIndex(2f)
            .align(Alignment.Center)
            .size(40.dp)
            .clip(CircleShape)
            .border(2.dp, Color.DarkGray, CircleShape)
    )
}

@Composable
fun BoxScope.MenuRow(menuIsVisible: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(1f)
            .align(Alignment.Center)
            .background(color = Color.Black)
            .drawBehind {
                val strokeWidth = 8f
                val x = size.width
                val y = size.height

                //top line
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(0f, 0f), //(0,0) at top-left point of the box
                    end = Offset(x, 0f), //top-right point of the box
                    strokeWidth = strokeWidth
                )

                //bottom line
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(0f, y),// bottom-left point of the box
                    end = Offset(x, y),// bottom-right point of the box
                    strokeWidth = strokeWidth
                )
            }
    ) {
        AnimatedVisibility(
            visible = menuIsVisible,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp, bottom = 18.dp)
            ) {
                Icon(Icons.Outlined.Replay, contentDescription = "reset", modifier = Modifier.size(36.dp), tint = Color.White)
                Icon(Icons.Outlined.Casino, contentDescription = "dice", modifier = Modifier.size(36.dp), tint = Color.White)
                Icon(Icons.Outlined.FavoriteBorder, contentDescription = "startingLife", modifier = Modifier.size(36.dp), tint = Color.White)
                Icon(Icons.Outlined.Settings, contentDescription = "settings", modifier = Modifier.size(36.dp), tint = Color.White)
            }
        }
    }
}

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
                    .offset(0.dp, 30.dp),
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