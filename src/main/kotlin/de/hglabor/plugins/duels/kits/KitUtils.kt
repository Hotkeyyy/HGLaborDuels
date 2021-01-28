package de.hglabor.plugins.duels.kits

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

object KitUtils {
    fun sword(type: Material, sharpness: Boolean): ItemStack {
        return itemStack(type) {
            amount = 1
            meta {
                isUnbreakable = true
                flag(ItemFlag.HIDE_UNBREAKABLE)

                if (sharpness) {
                    addEnchant(Enchantment.DAMAGE_ALL, 1, true)
                }
            }
        }
    }

    fun armor(player: Player, helmet: Material, chestplate: Material, leggings: Material, boots: Material) {
        player.inventory.helmet = ItemStack(helmet)
        player.inventory.chestplate = ItemStack(chestplate)
        player.inventory.leggings = ItemStack(leggings)
        player.inventory.boots = ItemStack(boots)
    }
    
    fun giveRecraft(player: Player, amount: Int) {
        player.inventory.setItem(13, ItemStack(Material.BROWN_MUSHROOM, amount))
        player.inventory.setItem(14, ItemStack(Material.RED_MUSHROOM, amount))
        player.inventory.setItem(15, ItemStack(Material.BOWL, amount))
    }

    fun giveSoups(player: Player) {
        for (i in 0..35) {
            if (player.inventory.getItem(i) == null) {
                player.inventory.setItem(i, ItemStack(Material.MUSHROOM_STEW))
            }
        }
    }

    fun goldenHead(itemAmount: Int): ItemStack {
        return itemStack(Material.GOLDEN_APPLE) {
            amount = itemAmount
            meta { name = "${KColors.GOLD}Golden Head" }
            mark("goldenHead")
        }
    }
}