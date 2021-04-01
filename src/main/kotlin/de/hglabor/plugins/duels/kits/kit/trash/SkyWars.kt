package de.hglabor.plugins.duels.kits.kit.trash

import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.KitCategory
import de.hglabor.plugins.duels.utils.KitUtils
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.specials.Specials
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionType

object SkyWars : AbstractKit() {

    override val name = "SkyWars"
    override val itemInGUI = Kits.guiItem(Material.CHEST, name)
    override val category = KitCategory.TRASH
    override val specials = setOf(Specials.ROD_KNOCKBACK, Specials.HUNGER)
    override val allowsRanked = true
    override val isMainKit = true

    override val armor = KitUtils.armor(
        itemStack(Material.DIAMOND_HELMET) {
            meta { addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1) } },
        itemStack(Material.DIAMOND_CHESTPLATE) {
            meta { addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2) } },
        itemStack(Material.DIAMOND_LEGGINGS) {
            meta { addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1) } },
        itemStack(Material.DIAMOND_BOOTS) {
            meta {
                addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                addEnchantment(Enchantment.PROTECTION_FALL, 4)
            }
        }
    )

    override val defaultInventory = mutableMapOf(
        0 to KitUtils.sword(Material.DIAMOND_SWORD, true, 2),
        1 to ItemStack(Material.FISHING_ROD),
        2 to ItemStack(Material.COBWEB, 12),
        3 to ItemStack(Material.LAVA_BUCKET),
        4 to KitUtils.potion(true, PotionType.INSTANT_HEAL, amount = 4),
        5 to ItemStack(Material.GOLDEN_APPLE, 2),
        6 to ItemStack(Material.STONE, 64),
        7 to ItemStack(Material.ENDER_PEARL),
        8 to ItemStack(Material.WATER_BUCKET),
        9 to ItemStack(Material.SNOWBALL, 16),
        10 to ItemStack(Material.BAKED_POTATO, 64),
        11 to ItemStack(ItemStack(Material.FLINT_AND_STEEL)),
        12 to itemStack(Material.DIAMOND_PICKAXE) { meta { addEnchantment(Enchantment.DIG_SPEED, 2) } },
        13 to itemStack(Material.DIAMOND_AXE) { meta { addEnchantment(Enchantment.DIG_SPEED, 2) } },
        24 to ItemStack(Material.STONE_BRICKS, 64),
        30 to ItemStack(Material.LAVA_BUCKET),
        31 to KitUtils.potion(true, PotionType.INSTANT_HEAL, amount = 3),
        33 to ItemStack(Material.BRICKS, 64),
        35 to ItemStack(Material.WATER_BUCKET)
    )
}