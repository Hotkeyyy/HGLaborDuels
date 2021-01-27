package de.hglabor.plugins.duels.utils

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.soupsimulator.Soupsimulator
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

object Data {
    val droppedItemInSoupsimulator = arrayListOf<Entity>()
    val soupsimulator = hashMapOf<Player, Soupsimulator>()

    val breakableBlocks = arrayListOf<Block>()

    val gameIDs = arrayListOf<String>()
    val inFight = arrayListOf<Player>()
    val openedDuelGUI = hashMapOf<Player, Player>()
    val openedQueue = hashMapOf<Player, Inventory>()
    val challenged = hashMapOf<Player, Player>() // Challenger Player
    val challengeKit: HashMap<Player, Kits> = HashMap()
    val duel = hashMapOf<Player, Player>()
    val duelIDFromPlayer = hashMapOf<Player, String>()
    val duelFromSpec = hashMapOf<Player, Duel>()
    val duelFromID = hashMapOf<String, Duel>()

    fun duelFromPlayer(player: Player): Duel {
        return duelFromID[duelIDFromPlayer[player]!!]!!
    }

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
        } while (true)

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

    val openedKitInventory = hashMapOf<Player, KitInventories>()
    enum class KitInventories { DUEL, TOURNAMENT, SPLITPARTY }
}

