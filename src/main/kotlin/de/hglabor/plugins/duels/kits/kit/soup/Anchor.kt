package de.hglabor.plugins.duels.kits.kit.soup

import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.utils.KitUtils
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object Anchor : AbstractKit() {

    override val name = "Anchor"
    override val itemInGUI = Kits.guiItem(Material.ANVIL, name)
    override val type = KitType.SOUP
    override val category = KitCategory.SOUP
    override val allowsRanked = true
    override val isMainKit = true

    override val defaultInventory = mutableMapOf(
        0 to KitUtils.sword(Material.STONE_SWORD, false),
        13 to ItemStack(Material.BROWN_MUSHROOM, 64),
        14 to ItemStack(Material.RED_MUSHROOM, 64),
        15 to ItemStack(Material.BOWL, 64)
    )

    override fun giveRest(player: Player) {
        KitUtils.fillEmptySlotsWithItemStack(player, ItemStack(Material.MUSHROOM_STEW))
    }

    override fun setAttributes(player: Player) {
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 100.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }
}