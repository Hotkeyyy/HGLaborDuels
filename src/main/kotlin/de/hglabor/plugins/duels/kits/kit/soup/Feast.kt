package de.hglabor.plugins.duels.kits.kit.soup

import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.utils.KitUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object Feast : AbstractKit() {

    override val name = "Feast"
    override val itemInGUI = Kits.guiItem(Material.DIAMOND_SWORD, name)
    override val type = KitType.SOUP
    override val category = KitCategory.SOUP
    override val isMainKit = true

    override val armor = KitUtils.armor(
        Material.IRON_HELMET,
        Material.IRON_CHESTPLATE,
        Material.IRON_LEGGINGS,
        Material.IRON_BOOTS
    )

    override val defaultInventory = mutableMapOf(
        0 to KitUtils.sword(Material.DIAMOND_SWORD, true),
        13 to ItemStack(Material.BROWN_MUSHROOM, 64),
        14 to ItemStack(Material.RED_MUSHROOM, 64),
        15 to ItemStack(Material.BOWL, 64)
    )

    override fun giveRest(player: Player) {
        KitUtils.fillEmptySlotsWithItemStack(player, ItemStack(Material.MUSHROOM_STEW))
    }
}