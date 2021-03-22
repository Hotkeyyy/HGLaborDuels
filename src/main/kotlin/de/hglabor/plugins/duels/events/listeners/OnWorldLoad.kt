package de.hglabor.plugins.duels.events.listeners

import de.hglabor.plugins.duels.utils.WorldManager
import net.axay.kspigot.event.listen
import org.bukkit.event.world.WorldLoadEvent

object OnWorldLoad {
    init {
        listen<WorldLoadEvent> {
            val world = it.world
            WorldManager.configureWorld(world)
        }
    }
}