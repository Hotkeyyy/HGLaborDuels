package de.hglabor.plugins.duels.kits.kit.uhc

import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.KitCategory
import de.hglabor.plugins.duels.utils.KitUtils
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.specials.Specials
import net.axay.kspigot.items.itemStack
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object UHC : AbstractKit() {

    override val name = "UHC"
    override val itemInGUI = Kits.guiItem(Material.GOLDEN_APPLE, name)
    override val category = KitCategory.UHC
    override val specials = setOf(Specials.ROD_KNOCKBACK)
    override val allowsRanked = true
    override val isMainKit = true

    override val armor = KitUtils.armor(
        itemStack(Material.DIAMOND_HELMET) { addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)},
        itemStack(Material.DIAMOND_CHESTPLATE) { addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)},
        itemStack(Material.IRON_LEGGINGS) { addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)},
        itemStack(Material.DIAMOND_BOOTS) { addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)}
    )

    override val defaultInventory = mutableMapOf(
        0 to itemStack(Material.DIAMOND_SWORD) { addEnchantment(Enchantment.DAMAGE_ALL, 3) },
        1 to ItemStack(Material.FISHING_ROD),
        2 to itemStack(Material.BOW) { addEnchantment(Enchantment.ARROW_DAMAGE, 2) },
        3 to ItemStack(Material.LAVA_BUCKET),
        4 to ItemStack(Material.GOLDEN_APPLE, 7),
        5 to ItemStack(KitUtils.goldenHead(4)),
        6 to ItemStack(Material.COBBLESTONE, 64),
        7 to ItemStack(Material.WATER_BUCKET),
        8 to ItemStack(Material.WATER_BUCKET),
        9 to ItemStack(Material.ARROW, 24),
        10 to ItemStack(Material.DIAMOND_AXE),
        11 to ItemStack(Material.DIAMOND_PICKAXE),
        12 to ItemStack(Material.OAK_PLANKS, 64)
    )
}

