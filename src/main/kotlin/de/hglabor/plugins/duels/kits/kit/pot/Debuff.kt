package de.hglabor.plugins.duels.kits.kit.pot

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.KitsGUI
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.kits.kit.`fun`.HardJumpAndRun
import de.hglabor.plugins.duels.kits.kit.cooldown.Classic
import de.hglabor.plugins.duels.kits.specials.Specials
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.items.flag
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.*

class Debuff : AbstractKit() {
    companion object {
        val INSTANCE = Debuff()

        fun enchantArmor(material: Material): ItemStack {
            return itemStack(material) {
                addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                addEnchantment(Enchantment.DURABILITY, 3)
                if (material.toString().toLowerCase().contains("boots"))
                    addEnchantment(Enchantment.PROTECTION_FALL, 4)
            }
        }
    }

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

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.inventory.helmet = enchantArmor(Material.DIAMOND_HELMET)
        player.inventory.chestplate = enchantArmor(Material.DIAMOND_CHESTPLATE)
        player.inventory.leggings = enchantArmor(Material.DIAMOND_LEGGINGS)
        player.inventory.boots = enchantArmor(Material.DIAMOND_BOOTS)

        player.inventory.setItem(0, KitUtils.sword(Material.DIAMOND_SWORD, true))
        player.inventory.setItem(1, ItemStack(Material.ENDER_PEARL, 16))

        for (i in setOf(8, 17, 26, 35)) {
            player.inventory.setItem(i, itemStack(Material.POTION) {
                meta<PotionMeta> { basePotionData = PotionData(PotionType.SPEED, false, true) }
            })
        }

        for (i in setOf(1, 27)) {
            player.inventory.setItem(i, itemStack(Material.SPLASH_POTION) {
                meta<PotionMeta> { basePotionData = PotionData(PotionType.POISON, false, false) }
            })
        }
        for (i in setOf(2, 28)) {
            player.inventory.setItem(i, itemStack(Material.SPLASH_POTION) {
                meta<PotionMeta> { basePotionData = PotionData(PotionType.SLOWNESS, false, false) }
            })
        }

        for (i in 0..35) {
            if (player.inventory.getItem(i) == null) {
                player.inventory.setItem(i, itemStack(Material.SPLASH_POTION) {
                    meta<PotionMeta> { basePotionData = PotionData(PotionType.INSTANT_HEAL, false, true) }
                })
            }
        }

        player.inventory.setItem(8, ItemStack(Material.GOLDEN_CARROT, 64))

        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    fun enable() {
        kits += INSTANCE
    }
}