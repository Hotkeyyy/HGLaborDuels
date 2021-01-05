package de.hglabor.plugins.duels.eventmanager

import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.broadcast
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerQuitEvent

object OnPlayerQuit {

    fun enable() {

        listen<PlayerQuitEvent>(EventPriority.HIGHEST) {
            val player = it.player
            it.quitMessage = null
            broadcast("${KColors.PALEVIOLETRED}← ${KColors.GRAY}${player.name}")

            if(player.isInFight()) {
                val duel = Data.duelFromID[Data.duelIDFromPlayer[player]]
                duel!!.winner = duel.getOtherPlayer(player)
                duel.loser = player
                duel.stop()
            }
        }
    }
}