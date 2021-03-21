package de.hglabor.plugins.duels.kits.kit.trash

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.KitsGUI
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.kits.kit.`fun`.HardJumpAndRun
import de.hglabor.plugins.duels.kits.kit.soup.Anchor
import net.axay.kspigot.items.itemStack
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Archer : AbstractKit() {
    companion object {
        val INSTANCE = Archer()
    }

    override val name = "Archer"
    override val itemInGUI = Kits.guiItem(Material.BOW, name)
    override val arenaTag = ArenaTags.NONE
    override val type = KitType.BOW
    override val allowsRespawn = false
    override val category = KitCategory.TRASH
    override val specials = setOf(null)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        KitUtils.armor(
            player, Material.LEATHER_HELMET, Material.CHAINMAIL_CHESTPLATE,
            Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS)

        player.inventory.setItem(0, itemStack(Material.BOW) {
            addEnchantment(Enchantment.ARROW_INFINITE, 1)
        })

        player.inventory.setItem(1, ItemStack(Material.GOLDEN_APPLE, 8))

        player.inventory.setItem(7, ItemStack(Material.IRON_AXE, 1))
        player.inventory.setItem(8, ItemStack(Material.OAK_PLANKS, 24))

        player.inventory.setItem(9, ItemStack(Material.ARROW))

        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    fun enable() {
        kits += INSTANCE
        Kits.mainKits += INSTANCE
    }
}