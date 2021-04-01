package de.hglabor.plugins.duels.kits.kit.cooldown

import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.KitCategory
import de.hglabor.plugins.duels.utils.KitUtils
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.specials.Specials
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

object Diamond : AbstractKit() {
    override val name = "Diamond"
    override val itemInGUI = Kits.guiItem(Material.DIAMOND_CHESTPLATE, name)
    override val category = KitCategory.COOLDOWN
    override val specials = setOf(Specials.HITCOOLDOWN)

    override val armor = KitUtils.armor(
        Material.DIAMOND_HELMET,
        Material.DIAMOND_CHESTPLATE,
        Material.DIAMOND_LEGGINGS,
        Material.DIAMOND_BOOTS
    )

    override val defaultInventory = mutableMapOf(0 to KitUtils.sword(Material.DIAMOND_SWORD, true))

    override fun setAttributes(player: Player) {
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 4.0
    }
}