package fr.mlac.mtglifecounter.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun MTGLifeCounterTheme(
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        colors = Colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}