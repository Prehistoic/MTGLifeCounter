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

class Player(var starting_lifepoints: Int) {

    var lifepoints by mutableStateOf(Lifepoints(starting_lifepoints, starting_lifepoints, 0))

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
}
