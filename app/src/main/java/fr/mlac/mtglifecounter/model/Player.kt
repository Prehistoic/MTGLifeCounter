package fr.mlac.mtglifecounter.model

import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import java.lang.Integer.max

data class Lifepoints(
    var current: Int,
    var starting: Int,
    var lastChange: Int
)

class Player(startingLifepoints: Int = 20) {

    var lifepoints by mutableStateOf(Lifepoints(startingLifepoints, startingLifepoints, 0))

    fun decreaseLifepoints() {
        if (lifepoints.current != 0) {
            lifepoints = lifepoints.copy(current = max(lifepoints.current - 1, 0).coerceAtLeast(-999), lastChange = lifepoints.lastChange - 1)
        }
    }

    fun increaseLifepoints() {
        lifepoints = lifepoints.copy(current = (lifepoints.current + 1).coerceAtMost(999), lastChange = lifepoints.lastChange + 1)
    }

    fun resetLifepoints() {
        lifepoints = lifepoints.copy(current = lifepoints.starting, lastChange = 0)
    }

    fun resetLastChange() {
        lifepoints = lifepoints.copy(lastChange = 0)
    }

    fun setStartingLifepoints(newValue: Int) {
        lifepoints = lifepoints.copy(starting = newValue, current = newValue, lastChange = 0)
    }
}
