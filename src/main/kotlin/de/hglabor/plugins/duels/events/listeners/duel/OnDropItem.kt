package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.event.listen
import net.axay.kspigot.runnables.task
import org.bukkit.Material
import org.bukkit.event.player.PlayerDropItemEvent

object OnDropItem {
    fun enable() {
        listen<PlayerDropItemEvent> {
            val player = it.player
            if (player.isInFight()) {
                if (it.itemDrop.itemStack.type == Material.BOWL) {
                    task(sync = false, delay = 20 * 8, period = 20, howOften = 1) {
                            _ ->
                        it.itemDrop.remove()
                    }
                }
            }
        }
    }
}