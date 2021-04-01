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

object IceFishing : AbstractKit() {

    override val name = "Ice Fishing"
    override val itemInGUI = Kits.guiItem(Material.FISHING_ROD, name)
    override val arenaTag = ArenaTags.ICEFISHING
    override val category = KitCategory.FUN
    override val isMainKit = true
    override val specials = setOf(Specials.NODAMAGE, Specials.DEADINWATER)

    override val defaultInventory = mutableMapOf(
        0 to itemStack(Material.FISHING_ROD) { amount = 1
            meta {isUnbreakable = true; flag(ItemFlag.HIDE_UNBREAKABLE) } }
    )
}