package de.hglabor.plugins.duels.kits.kit.soup

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.KitsGUI
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.kits.kit.`fun`.HardJumpAndRun
import de.hglabor.plugins.duels.kits.kit.cooldown.Classic
import net.axay.kspigot.items.flag
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Underwater : AbstractKit() {
    companion object {
        val INSTANCE = Underwater()
    }

    override val name = "Underwater"
    override val itemInGUI = Kits.guiItem(Material.TROPICAL_FISH_BUCKET, name)
    override val arenaTag = ArenaTags.UNDERWATER
    override val type = KitType.SOUP
    override val category = KitCategory.SOUP

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.inventory.helmet = ItemStack(Material.TURTLE_HELMET)
        player.inventory.setItem(0, KitUtils.sword(Material.IRON_SWORD, true))
        val trident = itemStack(Material.TRIDENT) {
            addEnchantment(Enchantment.LOYALTY, 3)
            meta {
                flag(ItemFlag.HIDE_ENCHANTS)
            }
        }
        player.inventory.setItem(1, trident)
        KitUtils.giveRecraft(player, 16)
        KitUtils.fillEmptySlotsWithSoup(player)

        player.addPotionEffect(PotionEffect(PotionEffectType.DOLPHINS_GRACE, Int.MAX_VALUE, 1))
        player.addPotionEffect(PotionEffect(PotionEffectType.CONDUIT_POWER, Int.MAX_VALUE, 0))

        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    fun enable() {
        kits += INSTANCE

    }
}