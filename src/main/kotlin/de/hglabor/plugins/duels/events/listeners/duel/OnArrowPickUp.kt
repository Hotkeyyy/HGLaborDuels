package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.player.DuelsPlayer
import net.axay.kspigot.event.listen
import org.bukkit.entity.Trident
import org.bukkit.event.player.PlayerPickupArrowEvent

object OnArrowPickUp {
    init {
        listen<PlayerPickupArrowEvent> {
            val player = it.player
            val duelsPlayer = DuelsPlayer.get(player)
            if (duelsPlayer.isSpectator())
                it.isCancelled = true
            if (it.arrow is Trident) {
                if (it.arrow.shooter == player) {
                    it.arrow.remove()
                }
            }
        }
    }
}