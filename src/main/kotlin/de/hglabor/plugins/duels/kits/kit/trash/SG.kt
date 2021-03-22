package de.hglabor.plugins.duels.kits.kit.trash

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.kits.specials.Specials
import net.axay.kspigot.items.itemStack
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class SG : AbstractKit() {
    companion object {
        val INSTANCE = SG()
    }

    override val name = "SG"
    override val itemInGUI = Kits.guiItem(Material.CHEST, name)
    override val category = KitCategory.TRASH
    override val specials = setOf(Specials.ROD_KNOCKBACK, Specials.HUNGER)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        KitUtils.armor(player, Material.IRON_HELMET, Material.IRON_CHESTPLATE,
            Material.CHAINMAIL_LEGGINGS, Material.GOLDEN_BOOTS)
        player.inventory.setItem(0, ItemStack(Material.STONE_SWORD))
        player.inventory.setItem(1, ItemStack(Material.FISHING_ROD))
        player.inventory.setItem(2, ItemStack(Material.BOW))
        player.inventory.setItem(3, ItemStack(Material.COBWEB, 3))
        player.inventory.setItem(4, itemStack(Material.FLINT_AND_STEEL) {
            durability = 2
        })
        player.inventory.setItem(5, ItemStack(Material.GOLDEN_APPLE, 1))
        player.inventory.setItem(6, ItemStack(Material.COOKED_BEEF, 4))
        player.inventory.setItem(8, ItemStack(Material.TNT, 2))
        player.inventory.setItem(9, ItemStack(Material.ARROW, 7))

        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    fun enable() {
        kits += INSTANCE
        Kits.mainKits += INSTANCE
    }
}