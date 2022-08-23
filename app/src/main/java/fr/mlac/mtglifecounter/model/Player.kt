package fr.mlac.mtglifecounter.model

import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import java.lang.Integer.max

data class Lifepoints(
    var current: Int,
    var starting: Int,
    var last_change: Int
)

class Player() {

    var lifepoints by mutableStateOf(Lifepoints(20, 20, 0))

    fun decreaseLifepoints() {
        lifepoints = lifepoints.copy(current = max(lifepoints.current -1, 0), last_change = lifepoints.last_change - 1)
    }

    fun increaseLifepoints() {
        lifepoints = lifepoints.copy(current = (lifepoints.current + 1).coerceAtMost(999), last_change = lifepoints.last_change + 1)
    }

    fun resetLifepoints() {
        lifepoints = lifepoints.copy(current = lifepoints.starting, last_change = 0)
    }

    fun resetLastChange() {
        lifepoints = lifepoints.copy(last_change = 0)
    }

    fun setStartingLifepoints(newValue: Int) {
        lifepoints = lifepoints.copy(starting = newValue, current = newValue, last_change = 0)
    }
}
