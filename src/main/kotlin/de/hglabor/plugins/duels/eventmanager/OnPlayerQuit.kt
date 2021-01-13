package de.hglabor.plugins.duels.eventmanager

import de.hglabor.plugins.duels.data.DataHolder
import de.hglabor.plugins.duels.data.PlayerSettings
import de.hglabor.plugins.duels.data.PlayerStats
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
            broadcast("${KColors.PALEVIOLETRED}← ${KColors.GRAY}${player.displayName}")

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
                    duel.loser = player
                    duel.winner = duel.getOtherPlayer(player)
                    duel.stop()
                    Data.challenged.remove(player)
                }
            }
        }
    }
}