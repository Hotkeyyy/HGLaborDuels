package de.hglabor.plugins.duels.eventmanager.arena

import net.axay.kspigot.event.listen
import org.bukkit.event.world.ChunkUnloadEvent

object OnChunkUnload {
    fun enable() {
        listen<ChunkUnloadEvent> {
            if (it.world.name == "FightWorld")
                it.chunk.entities.iterator().forEachRemaining { entity -> entity.remove() }
        }
    }
}