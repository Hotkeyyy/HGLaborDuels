package de.hglabor.plugins.duels.kits.kit.trash

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.KitCategory
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.specials.Specials
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

object Sumo : AbstractKit() {

    override val name = "Sumo"
    override val itemInGUI = Kits.guiItem(Material.LEAD, name)
    override val arenaTag = ArenaTags.SUMO
    override val category = KitCategory.TRASH
    override val specials = setOf(Specials.NODAMAGE, Specials.DEADINWATER)
    override val isMainKit = true

    override fun setAttributes(player: Player) {
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
    }
}