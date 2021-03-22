package de.hglabor.plugins.duels.kits.kit.`fun`

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.KitCategory
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.kits
import de.hglabor.plugins.duels.kits.specials.Specials
import net.axay.kspigot.items.flag
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

class IceFishing : AbstractKit() {
    companion object {
        val INSTANCE = IceFishing()
    }

    override val name = "Ice Fishing"
    override val itemInGUI = Kits.guiItem(Material.FISHING_ROD, name)
    override val arenaTag = ArenaTags.ICEFISHING
    override val category = KitCategory.FUN
    override val specials = setOf(Specials.NODAMAGE, Specials.DEADINWATER)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.inventory.setItem(0, itemStack(Material.FISHING_ROD) { amount = 1
            meta {isUnbreakable = true; flag(ItemFlag.HIDE_UNBREAKABLE) } })

        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    fun enable() {
        kits += INSTANCE
        Kits.mainKits += INSTANCE
    }
}