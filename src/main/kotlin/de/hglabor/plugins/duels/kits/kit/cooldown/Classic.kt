package de.hglabor.plugins.duels.kits.kit.cooldown

import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.KitCategory
import de.hglabor.plugins.duels.utils.KitUtils
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.specials.Specials
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object Classic : AbstractKit() {
    override val name = "Classic"
    override val itemInGUI = Kits.guiItem(Material.SHIELD, name)
    override val category = KitCategory.COOLDOWN
    override val specials = setOf(Specials.HITCOOLDOWN, Specials.HUNGER)

    override val armor = KitUtils.armor(
        Material.DIAMOND_HELMET,
        Material.DIAMOND_CHESTPLATE,
        Material.DIAMOND_LEGGINGS,
        Material.DIAMOND_BOOTS
    )
    override val offHand: ItemStack = ItemStack(Material.SHIELD)

    override val defaultInventory = mutableMapOf(
        0 to KitUtils.sword(Material.DIAMOND_SWORD, false),
        1 to KitUtils.sword(Material.IRON_AXE, false),
        2 to ItemStack(Material.BOW),
        3 to ItemStack(Material.CROSSBOW),
        4 to ItemStack(Material.BAKED_POTATO, 64),
        7 to ItemStack(Material.GOLDEN_APPLE, 12),
        8 to ItemStack(Material.ARROW, 32)
    )

    override fun setAttributes(player: Player) {
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 4.0
    }
}