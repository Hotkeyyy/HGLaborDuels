package de.hglabor.plugins.duels.events.listeners

import de.hglabor.plugins.duels.data.DataHolder
import de.hglabor.plugins.duels.events.events.duel.DuelDeathReason
import de.hglabor.plugins.duels.events.events.duel.PlayerDeathInDuelEvent
import de.hglabor.plugins.duels.party.Party
import de.hglabor.plugins.duels.party.Partys.isInParty
import de.hglabor.plugins.duels.party.Partys.isPartyMember
import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.duels.tournament.Tournaments
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.staff.utils.StaffData
import de.hglabor.plugins.staff.utils.StaffData.isVanished
import net.axay.kspigot.event.listen
import net.axay.kspigot.runnables.async
import org.bukkit.Bukkit
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerQuitEvent

object OnPlayerQuit {

    init {

        listen<PlayerQuitEvent>(EventPriority.HIGHEST) {
            val player = it.player
            val duelsPlayer = DuelsPlayer.get(player)
            it.quitMessage = null

            async {
                duelsPlayer.stats.update()
                duelsPlayer.settings.update()
                duelsPlayer.inventorySorting.update()
                DataHolder.playerStats.remove(player)
                DataHolder.playerSettings.remove(player)
                DataHolder.inventorySorting.remove(player)
            }

            if (player.isVanished)
                StaffData.vanishedPlayers.remove(player)

            if (duelsPlayer.isInFight()) {
                val duel = duelsPlayer.currentDuel() ?: return@listen
                Bukkit.getPluginManager().callEvent(PlayerDeathInDuelEvent(player, duel, DuelDeathReason.QUIT))
            }

            duelsPlayer.rankedQueues.forEach { kit ->
                kit.rankedQueue -= player
                duelsPlayer.rankedQueues -= kit
            }

            duelsPlayer.unrankedQueues.forEach { kit ->
                kit.unrankedQueue -= player
                duelsPlayer.unrankedQueues -= kit
            }


            /*if (Kits.playerQueue.containsKey(player)) {
                val kitSet = Kits.playerQueue[player]
                if (kitSet != null) {
                    for (kit in kitSet) {
                        val playerList = Kits.queue[kit]!!
                        playerList.remove(player)
                        Kits.playerQueue.remove(player)
                        Kits.queue[kit] = playerList
                    }
                }
            }*/

            if (player.isInParty()) {
                if (player.isPartyMember()) {
                    Party.get(player)!!.leave(player)
                } else {
                    Party.get(player)!!.delete()
                }
            }

            if (Tournaments.publicTournament != null) {
                if (Tournaments.publicTournament!!.players.contains(player)) {
                    Tournaments.publicTournament!!.players.remove(player)
                }
            }

            DuelsPlayer.duelsPlayers.remove(player)
            Data.clear(player)
        }
    }
}