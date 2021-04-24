package de.hglabor.plugins.duels.bots

import org.bukkit.entity.Player
import java.util.*


val Player.botSettings: BotOptions
    get() {
        return BotSettings.botSettings[this.uniqueId] ?: BotOptions()
    }

// kann man auch in datenbank packen ka ob sich lohnt
object BotSettings {
    val botSettings = hashMapOf<UUID, BotOptions>()
}

//TODO werte anpassen dass das so passt
class BotOptions(
    var health: Int = 100,
    var range: Double = 3.0,
    val followRange: Double = 200.0,
    var movementSpeed: Double = 0.33,
) {
    fun reset() {
        health = 100
        range = 3.0
        movementSpeed = 0.33
    }

    fun setDifficulty(difficulty: BotDifficulty) {
        when (difficulty) {
            BotDifficulty.EASY -> {
                health = 50
                range = 2.8
                movementSpeed = 0.30
            }
            BotDifficulty.MEDIUM -> {
                reset()
            }
            BotDifficulty.HARD -> {
                health = 125
                range = 3.1
                movementSpeed = 0.35
            }
            BotDifficulty.CHEATER -> {
                health = 150
                range = 3.3
                movementSpeed = 0.40
            }
        }
    }
}

enum class BotDifficulty {
    EASY,
    MEDIUM,
    HARD,
    CHEATER,
}