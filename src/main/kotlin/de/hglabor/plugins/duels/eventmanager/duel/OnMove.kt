package de.hglabor.plugins.duels.eventmanager.duel

import de.hglabor.plugins.duels.kits.Kits.Companion.info
import de.hglabor.plugins.duels.kits.Specials
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
                if (duel.kit.info.specials.contains(Specials.DEADINWATER)) {
                    if (!duel.hasEnded) {
                        if (player.isFeetInWater) {
                            duel.winner = duel.getOtherPlayer(player)
                            duel.loser = player
                            duel.stop()
                        }
                    }
                }
            }
        }
    }
}