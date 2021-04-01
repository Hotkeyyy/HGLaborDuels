package de.hglabor.plugins.duels.kits.kit.trash

import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.KitCategory
import de.hglabor.plugins.duels.kits.Kits
import net.axay.kspigot.items.flag
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

object OnlySword: AbstractKit() {

    override val name = "Only Sword"
    override val itemInGUI = Kits.guiItem(Material.GOLDEN_SWORD, name)
    override val category = KitCategory.TRASH
    override val allowsRanked = true
    override val isMainKit = true

    override val defaultInventory = mutableMapOf(
        0 to itemStack(Material.GOLDEN_SWORD) {
            meta {
                addEnchant(Enchantment.DAMAGE_ALL, 1, false)
                isUnbreakable = true
                flag(ItemFlag.HIDE_ATTRIBUTES)
            }
        }
    )

    override fun setAttributes(player: Player) {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 100.0
        player.health = 100.0
    }
}
