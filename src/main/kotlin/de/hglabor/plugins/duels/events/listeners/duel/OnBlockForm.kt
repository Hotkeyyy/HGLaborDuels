package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.utils.Data
import net.axay.kspigot.event.listen
import net.axay.kspigot.runnables.task
import org.bukkit.Material
import org.bukkit.event.block.BlockFormEvent

object OnBlockForm {
    init {
        listen<BlockFormEvent> {
            Data.breakableBlocks += it.block

            task(true, 1) { task ->
                if (it.block.type == Material.OBSIDIAN) {
                    val loc = it.block.location.add(0.0, -1.0, 0.0)
                    if (loc.block.isPassable) {
                        loc.block.type = Material.OBSIDIAN
                        Data.breakableBlocks += loc.block
                    }
                }
            }
        }
    }
}