package de.hglabor.plugins.duels.kits.kit.soup

import de.hglabor.plugins.duels.kits.*
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class Feast : AbstractKit() {
    companion object {
        val INSTANCE = Feast()
    }

    override val name = "Feast"
    override val itemInGUI = Kits.guiItem(Material.DIAMOND_SWORD, name)
    override val type = KitType.SOUP
    override val category = KitCategory.SOUP

    override fun giveKit(player: Player) {
        player.inventory.clear()
        KitUtils.armor(player, Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS)
        player.inventory.setItem(0, KitUtils.sword(Material.DIAMOND_SWORD, true))

        KitUtils.giveRecraft(player, 64)
        KitUtils.fillEmptySlotsWithSoup(player)

        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    fun enable() {
        kits += INSTANCE
        Kits.mainKits += INSTANCE
    }
}