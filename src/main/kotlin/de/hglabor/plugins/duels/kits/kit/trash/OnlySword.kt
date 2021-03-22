package de.hglabor.plugins.duels.kits.kit.trash

import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.KitCategory
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.kits
import net.axay.kspigot.items.flag
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

class OnlySword : AbstractKit() {
    companion object {
        val INSTANCE = OnlySword()
    }

    override val name = "Only Sword"
    override val itemInGUI = Kits.guiItem(Material.GOLDEN_SWORD, name)
    override val category = KitCategory.TRASH

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.inventory.addItem(itemStack(Material.GOLDEN_SWORD) {
            meta {
                addEnchant(Enchantment.DAMAGE_ALL, 1, false)
                isUnbreakable = true
                flag(ItemFlag.HIDE_ATTRIBUTES)
            }
        })

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 100.0
        player.health = 100.0
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    fun enable() {
        kits += INSTANCE
        Kits.mainKits += INSTANCE
    }
}
