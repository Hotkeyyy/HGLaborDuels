package de.hglabor.plugins.duels.kits.kit.cooldown

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.kits.kit.soup.Anchor
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

class Crystal : AbstractKit() {
    companion object {
        val INSTANCE = Crystal()
    }

    override val name = "Crystal"
    override val itemInGUI = Kits.guiItem(Material.END_CRYSTAL, name)
    override val arenaTag = ArenaTags.DEFAULT
    override val type = null
    override val allowsRespawn = false
    override val category = KitCategory.COOLDOWN
    override val specials = setOf(Specials.HITCOOLDOWN)

    override fun giveKit(player: Player) {
        player.inventory.clear()

        player.inventory.helmet = itemStack(Material.NETHERITE_HELMET) {
            meta {
                addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false)
                addEnchant(Enchantment.DURABILITY, 3, false)
                addEnchant(Enchantment.MENDING, 1, false)
            }
        }
        player.inventory.chestplate = itemStack(Material.NETHERITE_CHESTPLATE) {
            meta {
                addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false)
                addEnchant(Enchantment.DURABILITY, 3, false)
                addEnchant(Enchantment.MENDING, 1, false)
            }
        }
        player.inventory.leggings = itemStack(Material.NETHERITE_LEGGINGS) {
            meta {
                addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 4, false)
                addEnchant(Enchantment.DURABILITY, 3, false)
                addEnchant(Enchantment.MENDING, 1, false)
            }
        }
        player.inventory.boots = itemStack(Material.NETHERITE_BOOTS) {
            meta {
                addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 4, false)
                addEnchant(Enchantment.DURABILITY, 3, false)
                addEnchant(Enchantment.MENDING, 1, false)
                addEnchant(Enchantment.PROTECTION_FALL, 4, false)
            }
        }

        player.inventory.setItem(0, itemStack(Material.NETHERITE_SWORD) {
            meta {
                addEnchant(Enchantment.DAMAGE_ALL, 5, false)
                addEnchant(Enchantment.KNOCKBACK, 2, false)
                isUnbreakable = true
                flag(ItemFlag.HIDE_UNBREAKABLE)
            }
        })

        for (i in setOf(1, 9, 18, 27))
            player.inventory.setItem(i, ItemStack(Material.ENDER_PEARL, 16))

        player.inventory.setItem(2, ItemStack(Material.OBSIDIAN, 64))
        player.inventory.setItem(3, ItemStack(Material.END_CRYSTAL, 64))
        player.inventory.setItem(4, ItemStack(Material.GOLDEN_APPLE, 64))
        player.inventory.setItem(7, itemStack(Material.CROSSBOW) {
            meta {
                addEnchant(Enchantment.MULTISHOT, 1, false)
                addEnchant(Enchantment.QUICK_CHARGE, 3, false)
                isUnbreakable = true
                flag(ItemFlag.HIDE_UNBREAKABLE)
            }
        })
        player.inventory.setItem(10, itemStack(Material.TIPPED_ARROW) {
            amount = 64
            meta<PotionMeta> { basePotionData = PotionData(PotionType.SLOW_FALLING, true, false) }
        })
        player.inventory.setItem(11, ItemStack(Material.EXPERIENCE_BOTTLE, 64))
        player.inventory.setItem(16, itemStack(Material.TIPPED_ARROW) {
            amount = 64
            meta<PotionMeta> { basePotionData = PotionData(PotionType.SLOWNESS, false, true) }
        })

        val speedPot = itemStack(Material.SPLASH_POTION) {
            meta<PotionMeta> { basePotionData = PotionData(PotionType.SPEED, false, true) }
        }
        for (i in 19..25) {
            player.inventory.setItem(i, speedPot)
        }
        player.inventory.setItem(5, speedPot)

        val strengthPot = itemStack(Material.SPLASH_POTION) {
            meta<PotionMeta> { basePotionData = PotionData(PotionType.STRENGTH, false, true) }
        }
        for (i in 28..34) {
            player.inventory.setItem(i, strengthPot)
        }
        player.inventory.setItem(6, strengthPot)

        val fireresPot = itemStack(Material.SPLASH_POTION) {
            meta<PotionMeta> { basePotionData = PotionData(PotionType.FIRE_RESISTANCE, true, false) }
        }
        for (i in setOf(17, 26)) {
            player.inventory.setItem(i, fireresPot)
        }

        for (i in setOf(7, 12, 13, 14, 40)) {
            player.inventory.setItem(i, ItemStack(Material.TOTEM_OF_UNDYING))
        }

        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 4.0
    }

    fun enable() {
        kits += INSTANCE
        Kits.mainKits += INSTANCE
    }
}