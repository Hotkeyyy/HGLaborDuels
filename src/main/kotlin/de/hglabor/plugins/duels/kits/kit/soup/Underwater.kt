package de.hglabor.plugins.duels.kits.kit.soup

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.utils.KitUtils
import net.axay.kspigot.items.flag
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object Underwater : AbstractKit() {

    override val name = "Underwater"
    override val itemInGUI = Kits.guiItem(Material.TROPICAL_FISH_BUCKET, name)
    override val arenaTag = ArenaTags.UNDERWATER
    override val type = KitType.SOUP
    override val category = KitCategory.SOUP

    override val defaultInventory = mutableMapOf(
        0 to KitUtils.sword(Material.IRON_SWORD, true),
        1 to itemStack(Material.TRIDENT) {
            addEnchantment(Enchantment.LOYALTY, 3)
            meta {
                flag(ItemFlag.HIDE_ENCHANTS)
            }
        },
        13 to ItemStack(Material.BROWN_MUSHROOM, 32),
        14 to ItemStack(Material.RED_MUSHROOM, 32),
        15 to ItemStack(Material.BOWL, 32)
    )

    override fun giveRest(player: Player) {
        KitUtils.fillEmptySlotsWithItemStack(player, ItemStack(Material.MUSHROOM_STEW))
    }

    override fun setAttributes(player: Player) {
        player.addPotionEffect(PotionEffect(PotionEffectType.DOLPHINS_GRACE, Int.MAX_VALUE, 1))
        player.addPotionEffect(PotionEffect(PotionEffectType.CONDUIT_POWER, Int.MAX_VALUE, 0))
    }
}