package de.hglabor.plugins.duels.events.listeners.arena

import net.axay.kspigot.event.listen
import org.bukkit.entity.Player
import org.bukkit.event.world.ChunkUnloadEvent

object OnChunkUnload {
    init {
        listen<ChunkUnloadEvent> {
            if (it.world.name == "FightWorld")
                it.chunk.entities.forEach { entity ->
                    if (entity !is Player)
                        entity.remove()
                }
        }
    }
}