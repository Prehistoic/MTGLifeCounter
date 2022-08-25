package fr.mlac.mtglifecounter.ui.lifecounter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.mlac.mtglifecounter.model.Player
import kotlinx.coroutines.delay

@Composable
fun MenuRow(
    modifier: Modifier = Modifier,
    players: List<Player>,
    menuIsVisible: Boolean = false,
    onSettingsIconPressed: () -> Unit,
    setResetButtonIsPressed: (Boolean) -> Unit,
    setDiceButtonIsPressed: (Boolean) -> Unit,
    setChangeStartingLifepointsButtonIsPressed: (Boolean) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
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
                ResetButton(
                    players = players,
                    setResetButtonIsPressed = setResetButtonIsPressed
                )
                DiceButton(
                    players = players,
                    setDiceButtonIsPressed = setDiceButtonIsPressed
                )
                ChangeStartingLifepointsButton(
                    players = players,
                    setChangeStartingLifepointsButtonIsPressed = setChangeStartingLifepointsButtonIsPressed
                )
                SettingsButton(onSettingsIconPressed = onSettingsIconPressed)
            }
        }
    }
}

@Composable
fun startingLifepointsChoiceRow(
    modifier: Modifier = Modifier,
    players: List<Player>,
    menuIsVisible: Boolean = false,
    setChangeStartingLifepointsButtonIsPressed: (Boolean) -> Unit,
    setNewStartingLifepointsButtonIsPressed: (Boolean) -> Unit
) {
    // Failsafe pour retourner au menu de base si on quitte le menu à ce moment-là
    if (!menuIsVisible) {
        setChangeStartingLifepointsButtonIsPressed(false)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
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
                val startingLifepointsChoices = listOf(20, 25, 30, 40)

                startingLifepointsChoices.forEach() {
                    startingLifepointChoiceButton(
                        players = players,
                        startingLifepointChoice = it,
                        setChangeStartingLifepointsButtonIsPressed = setChangeStartingLifepointsButtonIsPressed,
                        setNewStartingLifepointsButtonIsPressed = setNewStartingLifepointsButtonIsPressed
                    )
                }
            }
        }
    }
}