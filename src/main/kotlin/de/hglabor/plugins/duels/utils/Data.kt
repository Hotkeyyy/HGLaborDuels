package de.hglabor.plugins.duels.utils

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.guis.KitsGUI
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.soupsimulator.Soupsimulator
import de.hglabor.plugins.staff.utils.StaffData
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

object Data {
    val droppedItemInSoupsimulator = arrayListOf<Entity>()
    val soupsimulator = hashMapOf<Player, Soupsimulator>()

    val breakableBlocks = arrayListOf<Block>()

    val gameIDs = arrayListOf<String>()
    val inFight = arrayListOf<Player>()
    val openedDuelGUI = hashMapOf<Player, Player>()
    val challenged = hashMapOf<Player, Player>() // Challenger Player
    val challengeKit: HashMap<Player, AbstractKit> = HashMap()
    val duel = hashMapOf<Player, Player>()
    val duelOfSpec = hashMapOf<Player, Duel>()
    val duelOfPlayer = hashMapOf<Player, Duel>()
    val duelFromID = hashMapOf<String, Duel>()

    val usedLocationMultipliersXZ = arrayListOf<Pair<Int, Int>>()
    const val locationMultiplier = 5000.0

    fun clear(player: Player) {
        openedDuelGUI.remove(player)
        challenged.remove(player)
        challengeKit.remove(player)
        duel.remove(player)
        duelOfSpec.remove(player)
        duelOfPlayer.remove(player)
        soupsimulator.remove(player)
        openedKitInventory.remove(player)
        StaffData.playersInStaffmode.remove(player)
        StaffData.vanishedPlayers.remove(player)
        StaffData.followingStaffFromPlayer.remove(player)
        StaffData.followedPlayerFromStaff.remove(player)
    }

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

    val openedKitInventory = hashMapOf<Player, KitsGUI.KitInventories>()
}

