package de.hglabor.plugins.duels.player

import de.hglabor.plugins.duels.data.InventorySorting
import de.hglabor.plugins.duels.data.PlayerSettings
import de.hglabor.plugins.duels.data.PlayerStats
import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.utils.Data
import org.bukkit.entity.Player

class DuelsPlayer(val bukkitPlayer: Player) {

    companion object {
        val duelsPlayers = mutableMapOf<Player, DuelsPlayer>()

        fun get(player: Player): DuelsPlayer {
            if (duelsPlayers.containsKey(player)) {
                if (duelsPlayers[player] != null) {
                    return duelsPlayers[player]!!
                }
            }
            val duelsPlayer = DuelsPlayer(player)
            duelsPlayers[player] = duelsPlayer
            return DuelsPlayer(player)
        }
    }

    val settings = PlayerSettings.get(bukkitPlayer)
    val stats = PlayerStats.get(bukkitPlayer)
    val inventorySorting = InventorySorting.get(bukkitPlayer)

    var unrankedQueues = setOf<AbstractKit>()
    var rankedQueues = setOf<AbstractKit>()


    fun isInFight() = Data.duelOfPlayer.containsKey(bukkitPlayer)
    fun isSpectator() = Data.duelOfSpec.containsKey(bukkitPlayer)
    fun isInSoupsimulator() = Data.soupsimulator.containsKey(bukkitPlayer)
    fun isBusy() = isInFight() || isSpectator() || isInSoupsimulator()

    fun currentDuel() = Data.duelOfPlayer[bukkitPlayer]
    fun spectatingDuel() = Data.duelOfSpec[bukkitPlayer]

    fun spectateDuel(duel: Duel, notifyPlayers: Boolean) {
        duel.addSpectator(bukkitPlayer, notifyPlayers)
    }
}