package fr.mlac.mtglifecounter.ui.lifecounter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import fr.mlac.mtglifecounter.model.Player

@Composable
fun MenuRow(
    modifier: Modifier = Modifier,
    players: List<Player>,
    menuIsVisible: Boolean,
    setMenuIsVisible: (Boolean) -> Unit,
    onSettingsIconPressed: () -> Unit,
    setResetButtonIsPressed: (Boolean) -> Unit,
    setDiceButtonIsPressed: (Boolean) -> Unit,
    changeStartingLifepointsButtonIsPressed: Boolean,
    setChangeStartingLifepointsButtonIsPressed: (Boolean) -> Unit,
    changePlayerCountButtonIsPressed: Boolean,
    setChangePlayerCountButtonIsPressed: (Boolean) -> Unit
) {
    val scrollState = rememberLazyListState()

    AnimatedVisibility(
        visible = menuIsVisible,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        if (!changeStartingLifepointsButtonIsPressed && !changePlayerCountButtonIsPressed) {
            BoxWithConstraints() {
                Icon(
                    Icons.AutoMirrored.Filled.NavigateBefore,
                    contentDescription = "scrollLeft",
                    modifier = modifier
                        .size(30.dp)
                        .align(Alignment.CenterStart),
                    tint = if (scrollState.firstVisibleItemIndex == 1) Color.White else Color.DarkGray
                )

                LazyRow(
                    state = scrollState,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 15.dp)
                        .align(Alignment.Center)
                        .zIndex(1f)
                ) {
                    item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.width(maxWidth / 4),
                        ) {
                            ResetButton(
                                players = players,
                                setResetButtonIsPressed = setResetButtonIsPressed,
                                setMenuIsVisible = setMenuIsVisible
                            )
                        }
                    }

                    item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = modifier.width(maxWidth / 4),
                        ) {
                            DiceButton(
                                setDiceButtonIsPressed = setDiceButtonIsPressed
                            )
                        }
                    }

                    item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = modifier.width(maxWidth / 4),
                        ) {
                            ChangePlayerCountButton(
                                setChangePlayerCountButtonIsPressed = setChangePlayerCountButtonIsPressed
                            )
                        }
                    }

                    item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = modifier.width(maxWidth / 4),
                        ) {
                            ChangeStartingLifepointsButton(
                                players = players,
                                setChangeStartingLifepointsButtonIsPressed = setChangeStartingLifepointsButtonIsPressed
                            )
                        }
                    }

                    item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = modifier.width(maxWidth / 4),
                        ) {
                            SettingsButton(
                                onSettingsIconPressed = onSettingsIconPressed
                            )
                        }
                    }
                }

                Icon(
                    Icons.AutoMirrored.Filled.NavigateNext,
                    contentDescription = "scrollRight",
                    modifier = modifier
                        .size(30.dp)
                        .align(Alignment.CenterEnd),
                    tint = if (scrollState.firstVisibleItemIndex == 0) Color.White else Color.DarkGray
                )

            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun startingLifepointsChoiceRow(
    modifier: Modifier = Modifier,
    players: List<Player>,
    menuIsVisible: Boolean,
    setMenuIsVisible: (Boolean) -> Unit,
    changeStartingLifepointsButtonIsPressed: Boolean,
    setChangeStartingLifepointsButtonIsPressed: (Boolean) -> Unit,
    setNewStartingLifepointsButtonIsPressed: (Boolean) -> Unit
) {
    // Failsafe pour retourner au menu de base si on quitte le menu à ce moment-là
    if (!menuIsVisible) {
        setChangeStartingLifepointsButtonIsPressed(false)
    }

    AnimatedVisibility(
        visible = menuIsVisible && changeStartingLifepointsButtonIsPressed,
        enter = scaleIn(),
        exit = shrinkVertically()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 15.dp)
        ) {
            val startingLifepointsChoices = listOf(20, 25, 30, 40)

            startingLifepointsChoices.forEach() {
                startingLifepointChoiceButton(
                    players = players,
                    startingLifepointChoice = it,
                    setChangeStartingLifepointsButtonIsPressed = setChangeStartingLifepointsButtonIsPressed,
                    setNewStartingLifepointsButtonIsPressed = setNewStartingLifepointsButtonIsPressed,
                    setMenuIsVisible = setMenuIsVisible
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun playerCountChoiceRow(
    modifier: Modifier = Modifier,
    players: List<Player>,
    setPlayers: (List<Player>) -> Unit,
    menuIsVisible: Boolean,
    setMenuIsVisible: (Boolean) -> Unit,
    changePlayerCountButtonIsPressed: Boolean,
    setChangePlayerCountButtonIsPressed: (Boolean) -> Unit,
    setNewStartingLifepointsButtonIsPressed: (Boolean) -> Unit
) {
    // Failsafe pour retourner au menu de base si on quitte le menu à ce moment-là
    if (!menuIsVisible) {
        setChangePlayerCountButtonIsPressed(false)
    }

    AnimatedVisibility(
        visible = menuIsVisible && changePlayerCountButtonIsPressed,
        enter = scaleIn(),
        exit = shrinkVertically()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 15.dp)
        ) {
            for (playerCount: Int in 2..4) {
                playerCountChoicesButton(
                    players = players,
                    setPlayers = setPlayers,
                    playerCountChoice = playerCount,
                    setNewStartingLifepointsButtonIsPressed = setNewStartingLifepointsButtonIsPressed,
                    setMenuIsVisible = setMenuIsVisible
                )
            }
        }
    }
}