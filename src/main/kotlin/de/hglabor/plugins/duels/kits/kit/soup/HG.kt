package de.hglabor.plugins.duels.kits.kit.soup

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.utils.KitUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class HG : AbstractKit() {
    companion object {
        val INSTANCE = HG()
    }

    override val name = "HG"
    override val itemInGUI = Kits.guiItem(Material.MUSHROOM_STEW, name)
    override val arenaTag = ArenaTags.HG
    override val type = KitType.SOUP
    override val category = KitCategory.SOUP
    override val allowsRanked = true
    override val isMainKit = true

    override val armor = KitUtils.armor(
        Material.IRON_HELMET,
        Material.IRON_CHESTPLATE,
        Material.IRON_LEGGINGS,
        Material.IRON_BOOTS
    )

    override val defaultInventory = mutableMapOf(
        0 to KitUtils.sword(Material.DIAMOND_SWORD, true),
        1 to ItemStack(Material.LAVA_BUCKET),
        2 to ItemStack(Material.COBBLESTONE_WALL, 64),
        7 to ItemStack(Material.WATER_BUCKET),
        8 to ItemStack(Material.OAK_PLANKS, 64),
        13 to ItemStack(Material.BOWL, 64),
        14 to ItemStack(Material.COCOA_BEANS, 64),
        16 to ItemStack(Material.IRON_PICKAXE),
        17 to ItemStack(Material.IRON_AXE)
    )

    override fun giveRest(player: Player) {
        KitUtils.fillEmptySlotsWithItemStack(player, ItemStack(Material.MUSHROOM_STEW))
    }
}