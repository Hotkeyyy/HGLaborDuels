package de.hglabor.plugins.duels.kits.kit.pot

import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.KitCategory
import de.hglabor.plugins.duels.utils.KitUtils
import de.hglabor.plugins.duels.kits.specials.Specials
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.items.flag
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType

object Debuff : AbstractKit() {

    override val name = "Debuff"
    override val itemInGUI = itemStack(Material.SPLASH_POTION) {
        meta<PotionMeta> {
            name = "${KColors.DEEPSKYBLUE}Debuff"
            basePotionData = PotionData(PotionType.POISON, false, false)
            flag(ItemFlag.HIDE_ATTRIBUTES)
        }
    }
    override val category = KitCategory.POT
    override val specials = setOf(Specials.PEARLCOOLDOWN, Specials.HUNGER)

    override val armor = KitUtils.armor(
        enchantArmor(Material.DIAMOND_HELMET),
        enchantArmor(Material.DIAMOND_CHESTPLATE),
        enchantArmor(Material.DIAMOND_LEGGINGS),
        enchantArmor(Material.DIAMOND_BOOTS)
    )

    override val defaultInventory = mutableMapOf(
        0 to KitUtils.sword(Material.DIAMOND_SWORD, true),
        1 to ItemStack(Material.ENDER_PEARL, 16),
        7 to KitUtils.potion(false, PotionType.SPEED, upgraded = true),
        17 to KitUtils.potion(false, PotionType.SPEED, upgraded = true),
        26 to KitUtils.potion(false, PotionType.SPEED, upgraded = true),
        35 to KitUtils.potion(false, PotionType.SPEED, upgraded = true),
        1 to KitUtils.potion(true, PotionType.POISON),
        27 to KitUtils.potion(true, PotionType.POISON),
        2 to KitUtils.potion(true, PotionType.SLOWNESS),
        28 to KitUtils.potion(true, PotionType.SLOWNESS),
        8 to ItemStack(Material.BAKED_POTATO, 64)
    )

    override fun giveRest(player: Player) {
        KitUtils.fillEmptySlotsWithItemStack(player, KitUtils.potion(true, PotionType.INSTANT_HEAL, upgraded = true))
    }

    private fun enchantArmor(material: Material) = itemStack(material) {
        addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        addEnchantment(Enchantment.DURABILITY, 3)
        if (material.toString().toLowerCase().contains("boots"))
            addEnchantment(Enchantment.PROTECTION_FALL, 4)
    }
}