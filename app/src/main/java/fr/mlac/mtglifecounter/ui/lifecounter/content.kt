package fr.mlac.mtglifecounter.ui.lifecounter

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import fr.mlac.mtglifecounter.model.Player
import fr.mlac.mtglifecounter.R
import kotlinx.coroutines.delay

@Composable
fun LifeCounterContent(
    players: List<Player>,
    onSettingsIconPressed: () -> Unit
) {
    val (menuIsVisible, setMenuVisible) = remember { mutableStateOf(false) }
    val yoffset : Float by animateFloatAsState(if (menuIsVisible) 1f else 0f)

    val (resetButtonIsPressed, setResetButtonIsPressed) = remember { mutableStateOf(false) }
    val (diceButtonIsPressed, setDiceButtonIsPressed) = remember { mutableStateOf(false) }
    val (changeStartingLifepointsButtonIsPressed, setChangeStartingLifepointsButtonIsPressed) = remember { mutableStateOf(false) }
    val (newStartingLifepointsButtonIsPressed, setNewStartingLifepointsButtonIsPressed) = remember { mutableStateOf(false)}

    Box() {
        MenuButton(
            modifier = Modifier
                .align(Alignment.Center)
                .zIndex(2f)
                .offset(0.dp, (-36 * yoffset).dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { setMenuVisible(!menuIsVisible) }
        )

        if (!changeStartingLifepointsButtonIsPressed) {
            MenuRow(
                modifier = Modifier
                    .align(Alignment.Center)
                    .zIndex(1f),
                players = players,
                menuIsVisible = menuIsVisible,
                onSettingsIconPressed = onSettingsIconPressed,
                setResetButtonIsPressed = setResetButtonIsPressed,
                setDiceButtonIsPressed = setDiceButtonIsPressed,
                setChangeStartingLifepointsButtonIsPressed = setChangeStartingLifepointsButtonIsPressed
            )
        } else {
            startingLifepointsChoiceRow(
                modifier = Modifier
                    .align(Alignment.Center)
                    .zIndex(1f),
                players = players,
                menuIsVisible = menuIsVisible,
                setChangeStartingLifepointsButtonIsPressed = setChangeStartingLifepointsButtonIsPressed,
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