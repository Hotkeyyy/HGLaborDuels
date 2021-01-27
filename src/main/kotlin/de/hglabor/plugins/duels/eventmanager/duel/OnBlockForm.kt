package de.hglabor.plugins.duels.eventmanager.duel

import de.hglabor.plugins.duels.utils.Data
import net.axay.kspigot.event.listen
import org.bukkit.event.block.BlockFormEvent

object OnBlockForm {
    fun enable() {
        listen<BlockFormEvent> {
            Data.breakableBlocks.add(it.block)
        }
    }
}