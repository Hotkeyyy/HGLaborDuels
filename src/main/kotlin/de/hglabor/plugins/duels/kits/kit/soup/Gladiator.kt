package de.hglabor.plugins.duels.kits.kit.soup

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.kits.*
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Gladiator : AbstractKit() {
    companion object {
        val INSTANCE = Gladiator()
    }

    override val name = "Gladiator"
    override val itemInGUI = Kits.guiItem(Material.IRON_BARS, name)
    override val arenaTag = ArenaTags.GLADIATOR
    override val type = KitType.SOUP
    override val category = KitCategory.SOUP

    override fun giveKit(player: Player) {
        player.inventory.clear()
        KitUtils.armor(player, Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS)
        player.inventory.setItem(0, KitUtils.sword(Material.DIAMOND_SWORD, true))

        player.inventory.setItem(1, ItemStack(Material.LAVA_BUCKET))
        player.inventory.setItem(16, ItemStack(Material.LAVA_BUCKET))
        player.inventory.setItem(2, ItemStack(Material.WATER_BUCKET))
        player.inventory.setItem(17, ItemStack(Material.WATER_BUCKET))
        player.inventory.setItem(7, ItemStack(Material.OAK_PLANKS, 64))
        player.inventory.setItem(8, ItemStack(Material.COBBLESTONE_WALL, 64))
        player.inventory.setItem(17, ItemStack(Material.IRON_PICKAXE))
        player.inventory.setItem(26, ItemStack(Material.IRON_AXE))
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