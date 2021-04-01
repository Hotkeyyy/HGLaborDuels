package de.hglabor.plugins.duels.kits.kit.soup

import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.utils.KitUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object Onebar : AbstractKit() {

    override val name = "Onebar"
    override val itemInGUI = Kits.guiItem(Material.WOODEN_SWORD, name)
    override val type = KitType.SOUP
    override val category = KitCategory.SOUP

    override val defaultInventory = mutableMapOf(
        0 to KitUtils.sword(Material.WOODEN_SWORD, false)
    )

    override fun giveRest(player: Player) {
        for (i in 1..8) {
            player.inventory.addItem(ItemStack(Material.MUSHROOM_STEW))
        }
    }
}