package de.hglabor.plugins.duels.eventmanager.duel

import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.isFeetInWater
import org.bukkit.event.player.PlayerMoveEvent

object OnMove {
    fun enable() {
        listen<PlayerMoveEvent> {
            val player = it.player
            if (player.isInFight()) {
                val duel = Data.duelFromPlayer(player)
                if (duel.kit == Kits.SUMO) {
                    if (!duel.hasEnded) {
                        if (player.isFeetInWater) {
                            duel.winner = duel.getOtherPlayer(player)
                            duel.loser = player
                            duel.stop()
                        }
                    }
                }
            }
            if (Data.frozenBecauseCountdown.contains(player)) {
                val from = it.from
                val to = it.to
                if (from.z != to.z && from.x != to.x) {
                    it.isCancelled = true
                }
            }
        }
    }
}