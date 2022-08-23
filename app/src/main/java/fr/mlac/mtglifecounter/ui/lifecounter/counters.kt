package fr.mlac.mtglifecounter.ui.lifecounter

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow

@Composable
fun LifepointChangeCounter(lifepoints_change: Int) {

    var valueToDisplay = lifepoints_change.toString()
    if (lifepoints_change > 0) {
        valueToDisplay = "+" + valueToDisplay
    }

    Text(
        text = valueToDisplay,
        style = MaterialTheme.typography.caption
    )
}

@Composable
fun LifepointCounter(lifepoints: String) {
    Text(
        text = lifepoints,
        style = MaterialTheme.typography.body1.copy(
            shadow = Shadow(
                color = MaterialTheme.colors.primaryVariant,
                offset = Offset(4f, 4f),
                blurRadius = 8f
            )
        )
    )
}