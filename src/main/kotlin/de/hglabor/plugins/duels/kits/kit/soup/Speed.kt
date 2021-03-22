package de.hglabor.plugins.duels.kits.kit.soup

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.kits.*
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

class Speed : AbstractKit() {
    companion object {
        val INSTANCE = Speed()
    }

    override val name = "Speed"
    override val itemInGUI = itemStack(Material.POTION) {
            meta<PotionMeta> {
                name = "${KColors.DEEPSKYBLUE}Speed"
                basePotionData = PotionData(PotionType.SPEED, false, true)
            }
        }

    override val type = KitType.SOUP
    override val category = KitCategory.SOUP

    override fun giveKit(player: Player) {
        player.inventory.clear()
        KitUtils.armor(player,
            Material.IRON_HELMET,
            Material.IRON_CHESTPLATE,
            Material.IRON_LEGGINGS,
            Material.IRON_BOOTS)
        player.inventory.setItem(0, KitUtils.sword(Material.DIAMOND_SWORD, true))

        KitUtils.giveRecraft(player, 32)
        KitUtils.fillEmptySlotsWithSoup(player)

        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 1))
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    fun enable() {
        kits += INSTANCE
    }
}