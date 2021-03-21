package de.hglabor.plugins.duels.kits.kit

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.kits.*
import org.bukkit.Material
import org.bukkit.entity.Player

class Random : AbstractKit() {
    companion object {
        val INSTANCE = Random()
    }

    override val name = "Random"
    override val itemInGUI = Kits.guiItem(Material.REPEATING_COMMAND_BLOCK, name)
    override val arenaTag = ArenaTags.NONE
    override val type = null
    override val allowsRespawn = false
    override val category = null
    override val specials = setOf(null)

    override fun giveKit(player: Player) { }

    fun enable() {
        kits += INSTANCE
    }
}