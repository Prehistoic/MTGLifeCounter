package fr.mlac.mtglifecounter.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import fr.mlac.mtglifecounter.ui.lifecounter.*

@Composable
fun SettingsContent(
    onBackButtonPressed: () -> Unit,
    soundIsEnabled: Boolean,
    setSoundIsEnabled: (Boolean) -> Unit
) {
    val settings = listOf("Rate This App", "Send Feedback", "About Us",
    "Sound Effects", "", "Remove Ads", "Restore Previous Purchases", "", "Legal",
    "Privacy Policy", "Privacy Settings")

    Column(
        modifier = Modifier.fillMaxWidth().background(color = Color.White)
    ) {
        Box(
            modifier = Modifier
                .background(color = Color.Red)
                .fillMaxWidth()
                .fillMaxHeight(0.07f)
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterStart),
                onClick = { onBackButtonPressed() }
            ) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "backToHome", modifier = Modifier.size(30.dp), tint = Color.White)
            }

            Text(
                modifier = Modifier.align(Alignment.Center).zIndex(1f),
                text = "Settings",
                style = MaterialTheme.typography.h2
            )
        }

        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(items = settings) {
                when (it) {
                    "Rate This App" -> {
                        settingsLinkButton(text = "Rate This App", link = "market://details?id=com.snowleopardgames.lifetotal")
                    }
                    "Send Feedback" -> {
                        settingsLinkButton(text = "Send Feedback", link = "https://talisman.games/contactus/?product=Magic%20Life%20Total&your-platform=Android")
                    }
                    "About Us" -> {
                        settingsLinkButton(text = "About Us", link = "https://talisman.games")
                    }
                    "Sound Effects" -> {
                        settingsSliderButton(
                            text = "Sound Effects",
                            property = soundIsEnabled,
                            setProperty = setSoundIsEnabled
                        )
                    }
                    "Remove Ads" -> {
                        settingsRotatingIconButton(
                            color = Color.Blue,
                            textLeft = "Remove Ads",
                            textRight = "2,09€"
                        )
                    }
                    "Restore Previous Purchases" -> {
                        settingsRotatingIconButton(
                            textLeft = "Restore Previous Purchases",
                            textRight = ""
                        )
                    }
                    "Legal" -> {
                        settingsModalOpenerButton(text = "Legal")
                    }
                    "Privacy Policy" -> {
                        settingsLinkButton(text = "Privacy Policy", link = "https://talisman.games/privacy-policy/magic-life-total-privacy-policy/")
                    }
                    "Privacy Settings" -> {
                        settingsModalOpenerButton(text = "Privacy Settings")
                    }
                    else -> {
                        settingsEntry() {
                            Text(" ")
                        }
                    }
                }
            }
        }
    }
}