package de.hglabor.plugins.duels.kits.kit.trash

import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.KitCategory
import de.hglabor.plugins.duels.utils.KitUtils
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.specials.Specials
import net.axay.kspigot.items.itemStack
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object SG : AbstractKit() {

    override val name = "SG"
    override val itemInGUI = Kits.guiItem(Material.CHEST, name)
    override val category = KitCategory.TRASH
    override val specials = setOf(Specials.ROD_KNOCKBACK, Specials.HUNGER)
    override val isMainKit = true

    override val armor = KitUtils.armor(
        Material.IRON_HELMET, 
        Material.IRON_CHESTPLATE,
        Material.CHAINMAIL_LEGGINGS, 
        Material.GOLDEN_BOOTS
    )
    
    override val defaultInventory = mutableMapOf(
        0 to ItemStack(Material.STONE_SWORD),
        1 to ItemStack(Material.FISHING_ROD),
        2 to ItemStack(Material.BOW),
        3 to ItemStack(Material.COBWEB, 3),
        4 to itemStack(Material.FLINT_AND_STEEL) { durability = 62 },
        5 to ItemStack(Material.GOLDEN_APPLE, 1),
        6 to ItemStack(Material.BAKED_POTATO, 7),
        7 to ItemStack(Material.ARROW, 7),
        8 to ItemStack(Material.TNT, 2)
    )
}