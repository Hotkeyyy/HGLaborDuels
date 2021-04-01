package de.hglabor.plugins.duels.kits.kit.soup

import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.utils.KitUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object EHG : AbstractKit() {

    override val name = "EHG"
    override val itemInGUI = Kits.guiItem(Material.STONE_SWORD, name)
    override val type = KitType.SOUP
    override val category = KitCategory.SOUP
    override val isMainKit = true

    override val defaultInventory = mutableMapOf(
        0 to KitUtils.sword(Material.STONE_SWORD, false),
        13 to ItemStack(Material.BROWN_MUSHROOM, 32),
        14 to ItemStack(Material.RED_MUSHROOM, 32),
        15 to ItemStack(Material.BOWL, 32)
    )

    override fun giveRest(player: Player) {
        KitUtils.fillEmptySlotsWithItemStack(player, ItemStack(Material.MUSHROOM_STEW))
    }
}