package de.hglabor.plugins.duels.kits.kit

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.Kits
import org.bukkit.Material

object Random : AbstractKit() {
    override val name = "Random"
    override val itemInGUI = Kits.guiItem(Material.REPEATING_COMMAND_BLOCK, name)
    override val arenaTag = ArenaTags.NONE
    override val type = null
    override val allowsRespawn = false
    override val category = null
    override val specials = setOf(null)
}