package fr.mlac.mtglifecounter.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import fr.mlac.mtglifecounter.ui.utils.KeepScreenOn

@Composable
fun MTGLifeCounterTheme(
    content: @Composable() () -> Unit
) {
    KeepScreenOn()

    MaterialTheme(
        colors = Colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}