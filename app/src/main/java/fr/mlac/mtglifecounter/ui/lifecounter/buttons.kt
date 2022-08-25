package fr.mlac.mtglifecounter.ui.lifecounter

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.mlac.mtglifecounter.R
import fr.mlac.mtglifecounter.model.Player
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MenuButton(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.center_logo),
        contentDescription = "logo",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .border(2.dp, Color.DarkGray, CircleShape)
    )
}

@Composable
fun ResetButton(
    modifier: Modifier = Modifier,
    players: List<Player>,
    setResetButtonIsPressed: (Boolean) -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = {
            setResetButtonIsPressed(true)
            players.forEach {
                it.resetLifepoints()
            }
        }
    ) {
        Icon(Icons.Outlined.Replay, contentDescription = "reset", modifier = Modifier
            .size(36.dp)
            .scale(scaleX = -1f, scaleY = 1f), tint = Color.White)
    }
}

@Composable
fun DiceButton(
    modifier: Modifier = Modifier,
    players: List<Player>,
    setDiceButtonIsPressed: (Boolean) -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = {
            setDiceButtonIsPressed(true)
        }
    ) {
        Icon(Icons.Outlined.Casino, contentDescription = "dice", modifier = Modifier.size(36.dp), tint = Color.White)
    }

    LaunchedEffect(Unit) {
        delay(1000)
        setDiceButtonIsPressed(false)
    }
}

@Composable
fun ChangeStartingLifepointsButton(
    modifier: Modifier = Modifier,
    players: List<Player>,
    setChangeStartingLifepointsButtonIsPressed: (Boolean) -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = {
            setChangeStartingLifepointsButtonIsPressed(true)
        }
    ) {
        Icon(Icons.Outlined.FavoriteBorder, contentDescription = "startingLife", modifier = Modifier.size(36.dp), tint = Color.White)
        Text(
            players[0].lifepoints.starting.toString(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.offset(x=0.dp, y=(-1).dp)
        )
    }
}

@Composable
fun SettingsButton(
    modifier: Modifier = Modifier,
    onSettingsIconPressed: () -> Unit
) {
    IconButton(
        onClick = { onSettingsIconPressed() }
    ) {
        Icon(Icons.Outlined.Settings, contentDescription = "settings", modifier = Modifier.size(36.dp), tint = Color.White)
    }
}

@Composable
fun startingLifepointChoiceButton(
    modifier: Modifier = Modifier,
    players: List<Player>,
    startingLifepointChoice: Int,
    setChangeStartingLifepointsButtonIsPressed: (Boolean) -> Unit,
    setNewStartingLifepointsButtonIsPressed: (Boolean) -> Unit
) {
    TextButton(
        onClick = {
            players.forEach() {
                it.setStartingLifepoints(startingLifepointChoice)
            }
            setChangeStartingLifepointsButtonIsPressed(false)
            setNewStartingLifepointsButtonIsPressed(true)
        },
        modifier = modifier
    ) {
        Text(
            text = startingLifepointChoice.toString(),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
    }
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