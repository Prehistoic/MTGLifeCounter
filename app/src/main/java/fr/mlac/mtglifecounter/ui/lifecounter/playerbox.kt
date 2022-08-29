package fr.mlac.mtglifecounter.ui.lifecounter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.mlac.mtglifecounter.R
import fr.mlac.mtglifecounter.model.Player

@Composable
fun HorizontalPlayerBox(
    modifier: Modifier = Modifier,
    player: Player,
    background: Int,
    setResetButtonIsPressed: (Boolean) -> Unit,
    setDiceButtonIsPressed:(Boolean) -> Unit,
    setChangeStartingLifepointsButtonIsPressed: (Boolean) -> Unit,
    setNewStartingLifepointsButtonIsPressed: (Boolean) -> Unit,
    resetButtonIsPressed: Boolean,
    diceButtonIsPressed: Boolean,
    changeStartingLifepointsButtonIsPressed: Boolean,
    newStartingLifepointsButtonIsPressed: Boolean
) {

    val lastChangeIsVisible = remember { MutableTransitionState(false) }
    val plusButtonClicked = remember { MutableInteractionSource() }
    val minusButtonClicked = remember { MutableInteractionSource() }
    var plusButtonPressed by remember { mutableStateOf(false) }
    var minusButtonPressed by remember { mutableStateOf(false) }

    if (!lastChangeIsVisible.currentState && lastChangeIsVisible.isIdle) {
        player.resetLastChange()
    }

    Box(
        modifier = modifier
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
                    LifepointChangeCounter(
                        lifepointsChange = player.lifepoints.last_change,
                        resetButtonIsPressed = resetButtonIsPressed,
                        newStartingLifepointsButtonIsPressed = newStartingLifepointsButtonIsPressed
                    )
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
                        Icon(
                            Icons.Outlined.RemoveCircleOutline,
                            "minus",
                            modifier = Modifier.size(50.dp),
                            tint = if (background != R.drawable.white_blur) Color.White else Color.DarkGray
                        )
                    } else {
                        Icon(
                            Icons.Filled.RemoveCircle,
                            "minus",
                            modifier = Modifier.size(50.dp),
                            tint = if (background != R.drawable.white_blur) Color.White else Color.DarkGray
                        )
                    }
                }

                LifepointCounter(
                    lifepoints = player.lifepoints.current.toString(),
                    setResetButtonIsPressed = setResetButtonIsPressed,
                    setNewStartingLifepointsButtonIsPressed = setNewStartingLifepointsButtonIsPressed,
                    resetButtonIsPressed = resetButtonIsPressed,
                    newStartingLifepointsButtonIsPressed = newStartingLifepointsButtonIsPressed
                )

                RepeatingButton(
                    modifier = Modifier.fillMaxHeight(),
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .offset(0.dp, (-36).dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val rotation by animateFloatAsState(
                    targetValue = if (resetButtonIsPressed || newStartingLifepointsButtonIsPressed) 180f else 0f,
                    animationSpec = tween(500)
                )
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "heart",
                    modifier = Modifier
                        .size(36.dp)
                        .graphicsLayer { rotationX = rotation }
                        .then(if (rotation > 90f) Modifier.scale(scaleX = 1f, scaleY = -1f) else Modifier),
                    tint = if (background != R.drawable.white_blur) Color.White else Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun VerticalPlayerBox(
    modifier: Modifier = Modifier,
    player: Player,
    background: Int,
    setResetButtonIsPressed: (Boolean) -> Unit,
    setDiceButtonIsPressed:(Boolean) -> Unit,
    setChangeStartingLifepointsButtonIsPressed: (Boolean) -> Unit,
    setNewStartingLifepointsButtonIsPressed: (Boolean) -> Unit,
    resetButtonIsPressed: Boolean,
    diceButtonIsPressed: Boolean,
    changeStartingLifepointsButtonIsPressed: Boolean,
    newStartingLifepointsButtonIsPressed: Boolean
) {
    val ratio: Float = 0.65f

    val lastChangeIsVisible = remember { MutableTransitionState(false) }
    val plusButtonClicked = remember { MutableInteractionSource() }
    val minusButtonClicked = remember { MutableInteractionSource() }
    var plusButtonPressed by remember { mutableStateOf(false) }
    var minusButtonPressed by remember { mutableStateOf(false) }

    if (!lastChangeIsVisible.currentState && lastChangeIsVisible.isIdle) {
        player.resetLastChange()
    }

    Box(
        modifier = modifier
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
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterStart)
                    .offset((ratio*30.0).dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                val rotation by animateFloatAsState(
                    targetValue = if (resetButtonIsPressed || newStartingLifepointsButtonIsPressed) 180f else 0f,
                    animationSpec = tween(500)
                )
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "heart",
                    modifier = Modifier
                        .size(36.0.dp)
                        .scale(ratio, ratio)
                        .rotate(90f)
                        .graphicsLayer { rotationX = rotation }
                        .then(
                            if (rotation > 90f) Modifier.scale(
                                scaleX = 1f,
                                scaleY = -1f
                            ) else Modifier
                        ),
                    tint = if (background != R.drawable.white_blur) Color.White else Color.DarkGray
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                AnimatedVisibility(
                    visibleState = lastChangeIsVisible,
                    enter = EnterTransition.None,
                    exit = fadeOut(
                        animationSpec = tween(delayMillis = 2500, easing = LinearOutSlowInEasing)
                    )
                ) {
                    LifepointChangeCounter(
                        modifier = Modifier
                            .rotate(90f)
                            .scale(ratio, ratio),
                        lifepointsChange = player.lifepoints.last_change,
                        resetButtonIsPressed = resetButtonIsPressed,
                        newStartingLifepointsButtonIsPressed = newStartingLifepointsButtonIsPressed
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                RepeatingButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(ratio, ratio),
                    onClick = { player.decreaseLifepoints() },
                    interactionSource = minusButtonClicked
                ) {
                    if (!minusButtonPressed) {
                        Icon(
                            Icons.Outlined.RemoveCircleOutline,
                            "minus",
                            modifier = Modifier.size(50.0.dp).rotate(90f),
                            tint = if (background != R.drawable.white_blur) Color.White else Color.DarkGray
                        )
                    } else {
                        Icon(
                            Icons.Filled.RemoveCircle,
                            "minus",
                            modifier = Modifier.size(50.0.dp).rotate(90f),
                            tint = if (background != R.drawable.white_blur) Color.White else Color.DarkGray
                        )
                    }
                }

                LifepointCounter(
                    modifier = Modifier
                        .rotate(90f)
                        .scale(ratio, ratio),
                    lifepoints = player.lifepoints.current.toString(),
                    setResetButtonIsPressed = setResetButtonIsPressed,
                    setNewStartingLifepointsButtonIsPressed = setNewStartingLifepointsButtonIsPressed,
                    resetButtonIsPressed = resetButtonIsPressed,
                    newStartingLifepointsButtonIsPressed = newStartingLifepointsButtonIsPressed,
                )

                RepeatingButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(ratio, ratio),
                    onClick = { player.increaseLifepoints() },
                    interactionSource = plusButtonClicked
                ) {
                    if (!plusButtonPressed) {
                        Icon(
                            Icons.Outlined.AddCircleOutline,
                            "plus",
                            modifier = Modifier.size(50.0.dp).rotate(90f),
                            tint = if (background != R.drawable.white_blur) Color.White else Color.DarkGray
                        )
                    } else {
                        Icon(
                            Icons.Filled.AddCircle,
                            "plus",
                            modifier = Modifier.size(50.0.dp).rotate(90f),
                            tint = if (background != R.drawable.white_blur) Color.White else Color.DarkGray
                        )
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