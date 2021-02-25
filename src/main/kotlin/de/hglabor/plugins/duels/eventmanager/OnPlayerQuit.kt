package de.hglabor.plugins.duels.eventmanager

import de.hglabor.plugins.duels.data.DataHolder
import de.hglabor.plugins.duels.data.PlayerSettings
import de.hglabor.plugins.duels.data.PlayerStats
import de.hglabor.plugins.duels.guis.QueueGUI
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.party.Party
import de.hglabor.plugins.duels.party.Partys.isInParty
import de.hglabor.plugins.duels.party.Partys.isPartyMember
import de.hglabor.plugins.duels.tournament.Tournaments
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.staff.utils.StaffData
import de.hglabor.plugins.staff.utils.StaffData.isVanished
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.runnables.async
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerQuitEvent

object OnPlayerQuit {

    fun enable() {

        listen<PlayerQuitEvent>(EventPriority.HIGHEST) {
            val player = it.player
            it.quitMessage = null

            async {
                val playerStats = PlayerStats.get(player)
                playerStats.update()
                DataHolder.playerStats.remove(player)

                val playerSettings = PlayerSettings.get(player)
                playerSettings.update()
                DataHolder.playerSettings.remove(player)
            }

            if (player.isVanished)
                StaffData.vanishedPlayers.remove(player)

            if (player.isInFight()) {
                if (Data.duelIDFromPlayer.containsKey(player)) {
                    val duel = Data.duelFromPlayer(player)
                    duel.playerLeft(player)
                    Data.challenged.remove(player)
                }
            }

            if (Kits.playerQueue.containsKey(player)) {
                val kit = Kits.playerQueue[player]
                val playerList = Kits.queue[kit]!!
                playerList.remove(player)
                Kits.playerQueue.remove(player)
                Kits.queue[kit!!] = playerList
                QueueGUI.updateContents()
            }

            if (player.isInParty()) {
                if (player.isPartyMember()) {
                    Party.get(player)!!.leave(player)
                } else {
                    Party.get(player)!!.delete()
                }
            }

            if (Tournaments.publicTournament != null)
                if (Tournaments.publicTournament!!.players.contains(player)) {
                    Tournaments.publicTournament!!.players.remove(player)
                }
        }
    }
}