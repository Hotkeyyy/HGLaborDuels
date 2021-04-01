package de.hglabor.plugins.duels.events.listeners

import de.hglabor.plugins.duels.player.DuelsPlayer
import net.axay.kspigot.event.listen
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent

object OnItemPickUp {
    init {
        listen<EntityPickupItemEvent> {
            if(it.entity is Player) {
                val duelsPlayer = DuelsPlayer.get(it.entity as Player)
                if(!duelsPlayer.isInFight()) {
                    it.isCancelled = true
                }
            }
        }
    }
}