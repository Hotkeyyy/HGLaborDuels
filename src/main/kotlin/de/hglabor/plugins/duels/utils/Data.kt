package de.hglabor.plugins.duels.utils

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.kits.Kits
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

object Data {
    val droppedItemInSoupsimulator = arrayListOf<Entity>()
    val gameIDs = arrayListOf<String>()

    val inFight = arrayListOf<Player>()
    val openedDuelGUI = hashMapOf<Player, Player>()

    val challenged = hashMapOf<Player, Player>() // Challenger Player
    val challengeKit: HashMap<Player, Kits> = HashMap()

    val duel = hashMapOf<Player, Player>()
    val duelIDFromPlayer = hashMapOf<Player, String>()
    val duelIDFromSpec = hashMapOf<Player, String>()
    val duelFromID = hashMapOf<String, Duel>()

    fun duelFromPlayer(player: Player): Duel {
        return duelFromID[duelIDFromPlayer[player]!!]!!
    }

    val frozenBecauseCountdown = arrayListOf<Player>()

    val usedLocationMultipliersXZ = arrayListOf<Pair<Int, Int>>()
    const val locationMultiplier = 5000.0

    fun getFreeLocation(): Pair<Int, Int> {
        var x = -15
        var z = -15

        do {
            if (!usedLocationMultipliersXZ.contains(Pair(x, z)))
                break
            if (x == z) {
                x++
                z = -15
            } else {
                z++
            }
        } while (true);

        return Pair(x, z)
    }

    fun getFreeGameID(): String {
        val finalID: String
        var id: String
        do {
            id = getRandomID()
        } while (gameIDs.contains(id))
        finalID = id
        return finalID
    }

    private fun getRandomID(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..5)
            .map { allowedChars.random() }
            .joinToString("")
    }
}

