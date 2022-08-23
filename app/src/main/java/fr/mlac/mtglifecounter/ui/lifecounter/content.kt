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

@Composable
fun LifeCounterContent(
    player1: Player,
    player2: Player,
    onSettingsIconClicked: () -> Unit
) {
    var menuIsVisible by remember { mutableStateOf(false) }
    val yoffset : Float by animateFloatAsState(if (menuIsVisible) 1f else 0f)

    Box() {
        MenuButton(
            modifier = Modifier
                .align(Alignment.Center)
                .zIndex(2f)
                .offset(0.dp, (-36 * yoffset).dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { menuIsVisible = !menuIsVisible }
        )

        MenuRow(
            modifier = Modifier
                .align(Alignment.Center)
                .zIndex(1f),
            menuIsVisible = menuIsVisible,
            onSettingsIconClicked = onSettingsIconClicked
        )

        Column(
            modifier = Modifier
        ) {
            PlayerBox(modifier = Modifier.scale(scaleX = -1f, scaleY = -1f), player1, R.drawable.blurred_blue)
            PlayerBox(modifier = Modifier, player2, R.drawable.blurred_fire)
        }
    }
}