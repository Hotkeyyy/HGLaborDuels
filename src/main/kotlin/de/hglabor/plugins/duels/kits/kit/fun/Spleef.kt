package de.hglabor.plugins.duels.kits.kit.`fun`

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.KitCategory
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.specials.Specials
import net.axay.kspigot.items.flag
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag

object Spleef: AbstractKit() {
    override val name = "de.hglabor.plugins.duels.kits.kit.`fun`.Spleef"
    override val itemInGUI = Kits.guiItem(Material.DIAMOND_SHOVEL, name)
    override val arenaTag = ArenaTags.SPLEEF
    override val category = KitCategory.FUN
    override val specials = setOf(Specials.INVINICIBLE, Specials.SPLEEF)

    override val defaultInventory = mutableMapOf(
        0 to itemStack(Material.DIAMOND_SHOVEL) {
            meta {
                isUnbreakable = true
                flag(ItemFlag.HIDE_UNBREAKABLE)
            }
        }
    )
}