package fr.mlac.mtglifecounter.ui.lifecounter

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import fr.mlac.mtglifecounter.model.Player
import fr.mlac.mtglifecounter.R
import kotlinx.coroutines.delay

@Composable
fun LifeCounterContent(
    players: List<Player>,
    setPlayers: (List<Player>) -> Unit,
    onSettingsIconPressed: () -> Unit
) {
    val (menuIsVisible, setMenuIsVisible) = remember { mutableStateOf(false) }
    val yoffset : Float by animateFloatAsState(if (menuIsVisible) 1f else 0f)

    val (resetButtonIsPressed, setResetButtonIsPressed) = remember { mutableStateOf(false) }
    val (diceButtonIsPressed, setDiceButtonIsPressed) = remember { mutableStateOf(false) }
    val (changeStartingLifepointsButtonIsPressed, setChangeStartingLifepointsButtonIsPressed) = remember { mutableStateOf(false) }
    val (newStartingLifepointsButtonIsPressed, setNewStartingLifepointsButtonIsPressed) = remember { mutableStateOf(false)}
    val (changePlayerCountButtonIsPressed, setChangePlayerCountButtonIsPressed) = remember { mutableStateOf(false)}

    Box() {
        MenuButton(
            modifier = Modifier
                .align(Alignment.Center)
                .zIndex(2f)
                .offset(0.dp, (-40 * yoffset).dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { setMenuIsVisible(!menuIsVisible) }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .zIndex(1f)
                .background(color = MaterialTheme.colors.primaryVariant)
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
            MenuRow(
                modifier = Modifier,
                players = players,
                menuIsVisible = menuIsVisible,
                setMenuIsVisible = setMenuIsVisible,
                onSettingsIconPressed = onSettingsIconPressed,
                setResetButtonIsPressed = setResetButtonIsPressed,
                setDiceButtonIsPressed = setDiceButtonIsPressed,
                changeStartingLifepointsButtonIsPressed = changeStartingLifepointsButtonIsPressed,
                setChangeStartingLifepointsButtonIsPressed = setChangeStartingLifepointsButtonIsPressed,
                changePlayerCountButtonIsPressed = changePlayerCountButtonIsPressed,
                setChangePlayerCountButtonIsPressed = setChangePlayerCountButtonIsPressed
            )

            startingLifepointsChoiceRow(
                modifier = Modifier,
                players = players,
                menuIsVisible = menuIsVisible,
                setMenuIsVisible = setMenuIsVisible,
                changeStartingLifepointsButtonIsPressed = changeStartingLifepointsButtonIsPressed,
                setChangeStartingLifepointsButtonIsPressed = setChangeStartingLifepointsButtonIsPressed,
                setNewStartingLifepointsButtonIsPressed = setNewStartingLifepointsButtonIsPressed
            )

            playerCountChoiceRow(
                modifier = Modifier,
                players = players,
                setPlayers = setPlayers,
                menuIsVisible = menuIsVisible,
                setMenuIsVisible = setMenuIsVisible,
                changePlayerCountButtonIsPressed = changePlayerCountButtonIsPressed,
                setChangePlayerCountButtonIsPressed = setChangePlayerCountButtonIsPressed,
                setNewStartingLifepointsButtonIsPressed = setNewStartingLifepointsButtonIsPressed
            )
        }

        Column(
            modifier = Modifier
        ) {
            PlayerBox(
                modifier = Modifier.scale(scaleX = -1f, scaleY = -1f),
                player = players[0],
                background = R.drawable.blurred_blue,
                setResetButtonIsPressed = setResetButtonIsPressed,
                setDiceButtonIsPressed = setDiceButtonIsPressed,
                setChangeStartingLifepointsButtonIsPressed = setChangeStartingLifepointsButtonIsPressed,
                setNewStartingLifepointsButtonIsPressed = setNewStartingLifepointsButtonIsPressed,
                resetButtonIsPressed = resetButtonIsPressed,
                diceButtonIsPressed = diceButtonIsPressed,
                changeStartingLifepointsButtonIsPressed = changeStartingLifepointsButtonIsPressed,
                newStartingLifepointsButtonIsPressed = newStartingLifepointsButtonIsPressed
            )
            PlayerBox(
                modifier = Modifier,
                player = players[1],
                background = R.drawable.blurred_fire,
                setResetButtonIsPressed = setResetButtonIsPressed,
                setDiceButtonIsPressed = setDiceButtonIsPressed,
                setChangeStartingLifepointsButtonIsPressed = setChangeStartingLifepointsButtonIsPressed,
                setNewStartingLifepointsButtonIsPressed = setNewStartingLifepointsButtonIsPressed,
                resetButtonIsPressed = resetButtonIsPressed,
                diceButtonIsPressed = diceButtonIsPressed,
                changeStartingLifepointsButtonIsPressed = changeStartingLifepointsButtonIsPressed,
                newStartingLifepointsButtonIsPressed = newStartingLifepointsButtonIsPressed
            )
        }
    }
}