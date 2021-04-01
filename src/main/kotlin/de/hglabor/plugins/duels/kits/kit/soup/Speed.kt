package de.hglabor.plugins.duels.kits.kit.soup

import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.KitCategory
import de.hglabor.plugins.duels.kits.KitType
import de.hglabor.plugins.duels.utils.KitUtils
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

object Speed : AbstractKit() {
    override val name = "Speed"
    override val itemInGUI = itemStack(Material.POTION) {
            meta<PotionMeta> {
                name = "${KColors.DEEPSKYBLUE}Speed"
                basePotionData = PotionData(PotionType.SPEED, false, true)
            }
        }

    override val type = KitType.SOUP
    override val category = KitCategory.SOUP

    override val armor = KitUtils.armor(
        Material.IRON_HELMET,
        Material.IRON_CHESTPLATE,
        Material.IRON_LEGGINGS,
        Material.IRON_BOOTS
    )

    override val defaultInventory = mutableMapOf(
        0 to KitUtils.sword(Material.DIAMOND_SWORD, true),
        13 to ItemStack(Material.BROWN_MUSHROOM, 32),
        14 to ItemStack(Material.RED_MUSHROOM, 32),
        15 to ItemStack(Material.BOWL, 32)
    )

    override fun giveRest(player: Player) {
        KitUtils.fillEmptySlotsWithItemStack(player, ItemStack(Material.MUSHROOM_STEW))
    }

    override fun setAttributes(player: Player) {
        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 1))
    }
}