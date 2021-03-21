package de.hglabor.plugins.duels.events.listeners

import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.event.listen
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent

object OnItemPickUp {
    fun enable() {
        listen<EntityPickupItemEvent> {
            if(it.entity is Player) {
                val player = it.entity as Player
                if(!player.isInFight()) {
                    it.isCancelled = true
                }
            }
        }
    }
}