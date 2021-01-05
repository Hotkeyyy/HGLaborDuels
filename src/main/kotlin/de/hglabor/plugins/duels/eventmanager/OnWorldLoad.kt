package de.hglabor.plugins.duels.eventmanager

import de.hglabor.plugins.duels.utils.WorldManager
import net.axay.kspigot.event.listen
import org.bukkit.event.world.WorldLoadEvent

object OnWorldLoad {
    fun enable() {
        listen<WorldLoadEvent> {
            val world = it.world
            WorldManager.configureWorld(world)
        }
    }
}