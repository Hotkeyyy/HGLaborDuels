package de.hglabor.plugins.duels.eventmanager.duel

import de.hglabor.plugins.duels.utils.PlayerFunctions.isSpectator
import net.axay.kspigot.event.listen
import org.bukkit.entity.Trident
import org.bukkit.event.player.PlayerPickupArrowEvent

object OnArrowPickUp {
    fun enable() {
        listen<PlayerPickupArrowEvent> {
            val player = it.player
            if (player.isSpectator())
                it.isCancelled = true
            if (it.arrow is Trident) {
                if (it.arrow.shooter == player) {
                    it.arrow.remove()
                }
            }
        }
    }
}