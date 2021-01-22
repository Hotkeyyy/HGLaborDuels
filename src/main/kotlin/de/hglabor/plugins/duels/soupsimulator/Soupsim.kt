package de.hglabor.plugins.duels.soupsimulator

import de.hglabor.plugins.duels.utils.Data
import org.bukkit.entity.Player

object Soupsim {
    fun Player.isInSoupsimulator(): Boolean { return Data.soupsimulator.containsKey(player) }

    val SoupsimulatorLevel.endsAfterTime get() = when(this) {
        SoupsimulatorLevel.EASY -> false
        else -> true
    }
}

enum class SoupsimulatorLevel { EASY, MEDIUM, HARD, BONUS; }
enum class SoupsimulatorTasks { SOUP, REFILL, RECRAFT; }