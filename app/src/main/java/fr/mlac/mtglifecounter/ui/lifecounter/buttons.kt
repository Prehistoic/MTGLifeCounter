package fr.mlac.mtglifecounter.ui.lifecounter

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.material.icons.outlined.PeopleAlt
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.sharp.GridView
import androidx.compose.material.icons.sharp.SpaceDashboard
import androidx.compose.material.icons.sharp.ViewAgenda
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
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
            .size(30.dp)
            .clip(CircleShape)
            .border(2.dp, Color.DarkGray, CircleShape)
    )
}

@Composable
fun ResetButton(
    modifier: Modifier = Modifier,
    players: List<Player>,
    setResetButtonIsPressed: (Boolean) -> Unit,
    setMenuIsVisible: (Boolean) -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = {
            setResetButtonIsPressed(true)
            setMenuIsVisible(false)
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
    setDiceButtonIsPressed: (Boolean) -> Unit
) {
    var currentDiceValue by remember { mutableIntStateOf((1..6).random()) }
    var resourceId by remember { mutableIntStateOf(0) }
    var shouldRoll by remember { mutableStateOf(false) }
    val rollFinished: (Float) -> Unit = {
        if (shouldRoll) currentDiceValue = (0..6).random()
        shouldRoll = false
    }

    IconButton(
        modifier = modifier,
        onClick = {
            setDiceButtonIsPressed(true)
            shouldRoll = true
        }
    ) {
        when (currentDiceValue) {
            1 -> resourceId = R.drawable.dice_1
            2 -> resourceId = R.drawable.dice_2
            3 -> resourceId = R.drawable.dice_3
            4 -> resourceId = R.drawable.dice_4
            5 -> resourceId = R.drawable.dice_5
            6 -> resourceId = R.drawable.dice_6
        }

        val rotation by animateFloatAsState(
            targetValue = if (shouldRoll) 360f else 0f,
            animationSpec = tween(500, easing = LinearEasing),
            finishedListener = rollFinished
        )

        Icon(ImageVector.vectorResource(
            id = resourceId),
            contentDescription = "dice",
            modifier = modifier.size(36.dp).graphicsLayer { rotationY = rotation },
            tint = Color.White)
    }

    LaunchedEffect(Unit) {
        delay(1000)
        setDiceButtonIsPressed(false)
    }
}

@Composable
fun ChangePlayerCountButton(
    modifier: Modifier = Modifier,
    setChangePlayerCountButtonIsPressed: (Boolean) -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = {
            setChangePlayerCountButtonIsPressed(true)
        }
    ) {
        Icon(
            Icons.Outlined.PeopleAlt,
            contentDescription = "playerCount",
            modifier = Modifier.size(36.dp),
            tint = Color.White
        )
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
    setNewStartingLifepointsButtonIsPressed: (Boolean) -> Unit,
    setMenuIsVisible: (Boolean) -> Unit
) {
    TextButton(
        onClick = {
            setMenuIsVisible(false)
            setNewStartingLifepointsButtonIsPressed(true)
            players.forEach() {
                it.setStartingLifepoints(startingLifepointChoice)
            }
        },
        modifier = modifier
    ) {
        Text(
            text = startingLifepointChoice.toString(),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
    }
}

@Composable
fun playerCountChoicesButton(
    modifier: Modifier = Modifier,
    players: List<Player>,
    setPlayers: (List<Player>) -> Unit,
    playerCountChoice: Int,
    setNewStartingLifepointsButtonIsPressed: (Boolean) -> Unit,
    setMenuIsVisible: (Boolean) -> Unit
) {
    IconButton(
        onClick = {
            setMenuIsVisible(false)
            setNewStartingLifepointsButtonIsPressed(true)
            val current_starting_lifepoints = players[0].lifepoints.starting
            setPlayers(mutableListOf<Player>().apply {
              repeat(playerCountChoice) { this.add(element = Player(current_starting_lifepoints)) }
            })
        },
        modifier = modifier
    ) {
        when (playerCountChoice) {
            2 -> {
                Icon(Icons.Sharp.ViewAgenda, contentDescription = "2players", modifier = Modifier.size(36.dp), tint = Color.White)
            }
            3 -> {
                Icon(Icons.Sharp.SpaceDashboard, contentDescription = "3players", modifier = Modifier
                    .size(36.dp)
                    .rotate(270f), tint = Color.White)
            }
            else -> {
                Icon(Icons.Sharp.GridView, contentDescription = "4players", modifier = Modifier.size(36.dp), tint = Color.White)
            }
        }

    }
}

@Composable
fun settingsEntry(
    modifier: Modifier = Modifier,
    content: @Composable @ExtensionFunctionType RowScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .drawBehind {
                val strokeWidth = 1f
                val x = size.width
                val y = size.height

                //bottom line
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, y),// bottom-left point of the box
                    end = Offset(x, y),// bottom-right point of the box
                    strokeWidth = strokeWidth
                )
            },
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 15.dp, end = 10.dp, bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            content = content
        )
    }
}

@Composable
fun settingsLinkButton(
    modifier: Modifier = Modifier,
    text: String,
    link: String
) {
    val context = LocalContext.current
    val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(link)) }

    settingsEntry(
        modifier = modifier.clickable(
            onClick = { context.startActivity(intent) }
        ),
    ) {
        Text(text = text, style = MaterialTheme.typography.h3)
        Icon(Icons.Outlined.NavigateNext, contentDescription = "goToLink",
            modifier=Modifier.size(30.dp),
            tint = Color.LightGray)
    }
}

@Composable
fun settingsSliderButton(
    modifier: Modifier = Modifier,
    text: String,
    property: Boolean,
    setProperty: (Boolean) -> Unit
) {
    settingsEntry(
        modifier = modifier.clickable(
            onClick = {  }
        ),
    ) {
        Text(text = text, style = MaterialTheme.typography.h3)
    }
}

@Composable
fun settingsTextOnlyButton(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    textLeft: String = "",
    textRight: String = ""
) {
    settingsEntry(
        modifier = modifier.clickable(
            onClick = {  }
        ),
    ) {
        Text(text = textLeft, style = MaterialTheme.typography.h3, color = color)
        Text(text = textRight, style = MaterialTheme.typography.h3, color = color)
    }
}

@Composable
fun settingsLoadingIndicatorButton(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    textLeft: String = "",
    textRight: String = ""
) {
    var displayLoadingIndicator by remember { mutableStateOf(false) }
    var rotations by remember { mutableStateOf(0) }

    settingsEntry(
        modifier = modifier.clickable(
            onClick = { displayLoadingIndicator = true }
        ),
    ) {
        Text(text = textLeft, style = MaterialTheme.typography.h3, color = color)

        if (!displayLoadingIndicator) {
            Text(text = textRight, style = MaterialTheme.typography.h3, color = color)
        } else {
            CircularProgressIndicator(
                modifier=Modifier.size(20.dp),
                color = Color.Blue
            )

            if (rotations < 100) rotations++
            else {
                rotations = 0
                displayLoadingIndicator = false
            }
        }
    }
}

@Composable
fun settingsModalOpenerButton(
    modifier: Modifier = Modifier,
    text: String = ""
) {
    settingsEntry(
        modifier = modifier.clickable(
            onClick = {  }
        ),
    ) {
        Text(text = text, style = MaterialTheme.typography.h3)
        Icon(Icons.Outlined.NavigateNext, contentDescription = "next",
            modifier=Modifier.size(30.dp),
            tint = Color.LightGray)
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
            },
        onClick = {},
        enabled = enabled,
        interactionSource = interactionSource,
        content = content
    )
}