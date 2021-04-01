package de.hglabor.plugins.duels.utils

import net.axay.kspigot.chat.KColors
import net.axay.kspigot.items.flag
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.utils.mark
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType

object KitUtils {
    enum class ArmorSlots(val number: Int) {
        HELMET(39), CHESTPLATE(38), LEGGINGS(37), BOOTS(36)
    }

    fun sword(type: Material, sharpness: Boolean, level: Int = 1): ItemStack {
        return itemStack(type) {
            amount = 1
            meta {
                isUnbreakable = true
                flag(ItemFlag.HIDE_UNBREAKABLE)

                if (sharpness) {
                    addEnchant(Enchantment.DAMAGE_ALL, level, true)
                }
            }
        }
    }

    fun armor(helmet: Material?, chestplate: Material?, leggings: Material?, boots: Material?) = mutableMapOf(
        ArmorSlots.HELMET to item(helmet),
        ArmorSlots.CHESTPLATE to item(chestplate),
        ArmorSlots.LEGGINGS to item(leggings),
        ArmorSlots.BOOTS to item(boots)
    )

    fun armor(helmet: ItemStack?, chestplate: ItemStack?, leggings: ItemStack?, boots: ItemStack?) = mutableMapOf(
        ArmorSlots.HELMET to helmet,
        ArmorSlots.CHESTPLATE to chestplate,
        ArmorSlots.LEGGINGS to leggings,
        ArmorSlots.BOOTS to boots
    )

    fun giveRecraft(player: Player, amount: Int) {
        player.inventory.setItem(13, ItemStack(Material.BROWN_MUSHROOM, amount))
        player.inventory.setItem(14, ItemStack(Material.RED_MUSHROOM, amount))
        player.inventory.setItem(15, ItemStack(Material.BOWL, amount))
    }

    fun fillEmptySlotsWithItemStack(player: Player, itemStack: ItemStack) {
        for (i in 0..35) {
            if (player.inventory.getItem(i) == null) {
                player.inventory.setItem(i, ItemStack(itemStack))
            }
        }
    }

    fun goldenHead(itemAmount: Int) = itemStack(Material.GOLDEN_APPLE) {
        amount = itemAmount
        meta { name = "${KColors.GOLD}Golden Head" }
        mark("goldenHead")
    }

    fun item(material: Material?) =
        if (material == null) null
        else ItemStack(material)

    fun potion(
        splash: Boolean,
        type: PotionType,
        extended: Boolean = false,
        upgraded: Boolean = false,
        amount: Int = 1
    ) = itemStack(if (splash) Material.SPLASH_POTION else Material.POTION) {
        this.amount = amount
        meta<PotionMeta> {
            basePotionData = PotionData(type, extended, upgraded)
        }
    }
}

