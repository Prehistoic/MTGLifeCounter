package fr.mlac.mtglifecounter.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import fr.mlac.mtglifecounter.R

val fonts = FontFamily(
    Font(R.font.fester_regular)
)

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        color = Color.White,
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 180.sp
    ),
    caption = TextStyle(
        color = Color.White,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 60.sp
    ),
    h2 = TextStyle(
        color = Color.White,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    h3 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    )
)