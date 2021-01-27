package de.hglabor.plugins.duels.eventmanager

import net.axay.kspigot.event.listen
import org.bukkit.event.entity.FoodLevelChangeEvent

object OnFoodLevelChange {
    fun enable() {
        listen<FoodLevelChangeEvent> {
            it.isCancelled = true
        }
    }
}