package de.hglabor.plugins.duels.eventmanager

import net.axay.kspigot.event.listen
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.FoodLevelChangeEvent

object OnFoodLevelChange {
    fun enable() {
        listen<FoodLevelChangeEvent> {
            it.isCancelled = true
        }

        listen<EntityRegainHealthEvent> {
            if (it.regainReason == EntityRegainHealthEvent.RegainReason.EATING || it.regainReason == EntityRegainHealthEvent.RegainReason.SATIATED) {
                it.isCancelled = true
            }
        }
    }
}