package de.hglabor.plugins.soupsimulator

object Soupsim {
    val SoupsimulatorLevel.endsAfterTime get() = when(this) {
        SoupsimulatorLevel.EASY -> false
        else -> true
    }
}

enum class SoupsimulatorLevel { EASY, MEDIUM, HARD, BONUS; }
enum class SoupsimulatorTasks { SOUP, REFILL, RECRAFT; }