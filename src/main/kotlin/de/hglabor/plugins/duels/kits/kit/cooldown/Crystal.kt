package de.hglabor.plugins.duels.kits.kit.cooldown

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.KitCategory
import de.hglabor.plugins.duels.utils.KitUtils
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.specials.Specials
import net.axay.kspigot.items.flag
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType

object Crystal : AbstractKit() {

    override val name = "Crystal"
    override val itemInGUI = Kits.guiItem(Material.END_CRYSTAL, name)
    override val arenaTag = ArenaTags.DEFAULT
    override val category = KitCategory.COOLDOWN
    override val specials = setOf(Specials.HITCOOLDOWN)
    override val isMainKit = true
    override val allowsRanked = true

    override val armor = KitUtils.armor(
        itemStack(Material.NETHERITE_HELMET) {
            meta {
                addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false)
                addEnchant(Enchantment.DURABILITY, 3, false)
                addEnchant(Enchantment.MENDING, 1, false)
            }
        },
        itemStack(Material.NETHERITE_CHESTPLATE) {
            meta {
                addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false)
                addEnchant(Enchantment.DURABILITY, 3, false)
                addEnchant(Enchantment.MENDING, 1, false)
            }
        },
        itemStack(Material.NETHERITE_LEGGINGS) {
            meta {
                addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 4, false)
                addEnchant(Enchantment.DURABILITY, 3, false)
                addEnchant(Enchantment.MENDING, 1, false)
            }
        },
        itemStack(Material.NETHERITE_BOOTS) {
            meta {
                addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 4, false)
                addEnchant(Enchantment.DURABILITY, 3, false)
                addEnchant(Enchantment.MENDING, 1, false)
                addEnchant(Enchantment.PROTECTION_FALL, 4, false)
            }
        }
    )

    override val offHand = ItemStack(Material.TOTEM_OF_UNDYING)

    override val defaultInventory = mutableMapOf(
        0 to itemStack(Material.NETHERITE_SWORD) {
            meta {
                addEnchant(Enchantment.DAMAGE_ALL, 5, false)
                addEnchant(Enchantment.KNOCKBACK, 2, false)
                isUnbreakable = true
                flag(ItemFlag.HIDE_UNBREAKABLE)
            }
        },

        1 to ItemStack(Material.ENDER_PEARL, 16),
        9 to ItemStack(Material.ENDER_PEARL, 16),
        18 to ItemStack(Material.ENDER_PEARL, 16),
        27 to ItemStack(Material.ENDER_PEARL, 16),
        2 to ItemStack(Material.OBSIDIAN, 64),
        3 to ItemStack(Material.END_CRYSTAL, 64),
        4 to ItemStack(Material.GOLDEN_APPLE, 64),
        8 to itemStack(Material.CROSSBOW) {
            meta {
                addEnchant(Enchantment.MULTISHOT, 1, false)
                addEnchant(Enchantment.QUICK_CHARGE, 3, false)
                isUnbreakable = true
                flag(ItemFlag.HIDE_UNBREAKABLE)
            }
        },
        10 to itemStack(Material.TIPPED_ARROW) {
            amount = 64
            meta<PotionMeta> { basePotionData = PotionData(PotionType.SLOW_FALLING, true, false) }
        },
        11 to ItemStack(Material.EXPERIENCE_BOTTLE, 64),
        16 to itemStack(Material.TIPPED_ARROW) {
            amount = 64
            meta<PotionMeta> { basePotionData = PotionData(PotionType.SLOWNESS, false, true) }
        },
        5 to KitUtils.potion(true, PotionType.SPEED, upgraded = true),
        19 to KitUtils.potion(true, PotionType.SPEED, upgraded = true),
        20 to KitUtils.potion(true, PotionType.SPEED, upgraded = true),
        21 to KitUtils.potion(true, PotionType.SPEED, upgraded = true),
        22 to KitUtils.potion(true, PotionType.SPEED, upgraded = true),
        23 to KitUtils.potion(true, PotionType.SPEED, upgraded = true),
        24 to KitUtils.potion(true, PotionType.SPEED, upgraded = true),
        25 to KitUtils.potion(true, PotionType.SPEED, upgraded = true),
        19 to KitUtils.potion(true, PotionType.SPEED, upgraded = true),
        6 to KitUtils.potion(true, PotionType.STRENGTH, upgraded = true),
        28 to KitUtils.potion(true, PotionType.STRENGTH, upgraded = true),
        29 to KitUtils.potion(true, PotionType.STRENGTH, upgraded = true),
        30 to KitUtils.potion(true, PotionType.STRENGTH, upgraded = true),
        31 to KitUtils.potion(true, PotionType.STRENGTH, upgraded = true),
        32 to KitUtils.potion(true, PotionType.STRENGTH, upgraded = true),
        33 to KitUtils.potion(true, PotionType.STRENGTH, upgraded = true),
        34 to KitUtils.potion(true, PotionType.STRENGTH, upgraded = true),
        7 to ItemStack(Material.TOTEM_OF_UNDYING),
        12 to ItemStack(Material.TOTEM_OF_UNDYING),
        13 to ItemStack(Material.TOTEM_OF_UNDYING),
        14 to ItemStack(Material.TOTEM_OF_UNDYING)
    )

    override fun setAttributes(player: Player) {
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 4.0
    }
}