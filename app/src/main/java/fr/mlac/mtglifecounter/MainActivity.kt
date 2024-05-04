package fr.mlac.mtglifecounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.mlac.mtglifecounter.model.Player
import fr.mlac.mtglifecounter.ui.lifecounter.LifeCounterContent
import fr.mlac.mtglifecounter.ui.settings.SettingsContent
import fr.mlac.mtglifecounter.ui.theme.MTGLifeCounterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MTGLifeCounterTheme() {

                val (soundIsEnabled, setSoundIsEnabled) = remember { mutableStateOf(true) }

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.LifeCounter.route) {
                    composable(Routes.LifeCounter.route) {
                        LifeCounterScreen(
                            onSettingsIconPressed = {
                                navController.navigate(Routes.Settings.route)
                            }
                        )
                    }

                    composable(Routes.Settings.route) {
                        SettingsScreen(
                            onBackButtonPressed = {
                                navController.navigate(Routes.LifeCounter.route)
                            },
                            soundIsEnabled = soundIsEnabled,
                            setSoundIsEnabled = setSoundIsEnabled
                        )
                    }
                }
            }
        }
    }
}

sealed class Routes(val route: String) {
    object LifeCounter : Routes("lifecounter")
    object Settings : Routes("settings")
}

@Composable
fun LifeCounterScreen(
    onSettingsIconPressed: () -> Unit,
) {
    val (players, setPlayers) = remember { mutableStateOf(listOf(Player(20), Player(20))) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
    ) {
        LifeCounterContent(players, setPlayers, onSettingsIconPressed)
    }
}

@Composable
fun SettingsScreen(
    onBackButtonPressed: () -> Unit,
    soundIsEnabled: Boolean,
    setSoundIsEnabled: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
    ) {
        SettingsContent(onBackButtonPressed, soundIsEnabled, setSoundIsEnabled)
    }
}

