package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.player.DuelsPlayer
import net.axay.kspigot.event.listen
import net.axay.kspigot.runnables.task
import org.bukkit.Material
import org.bukkit.event.player.PlayerDropItemEvent

object OnDropItem {
    init {
        listen<PlayerDropItemEvent> {
            val player = it.player
            val duelsPlayer = DuelsPlayer.get(player)
            if (duelsPlayer.isInFight()) {
                if (it.itemDrop.itemStack.type == Material.BOWL) {
                    task(sync = false, delay = 20 * 8) { _ ->
                        it.itemDrop.remove()
                    }
                }
            }
        }
    }
}