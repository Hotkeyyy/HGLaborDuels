package de.hglabor.plugins.duels.soupsimulator

object Soupsim {
    val SoupsimulatorLevel.endsAfterTime get() = when(this) {
        SoupsimulatorLevel.EASY -> false
        else -> true
    }
}

enum class SoupsimulatorLevel { EASY, MEDIUM, HARD, BONUS; }
enum class SoupsimulatorTasks { SOUP, REFILL, RECRAFT; }