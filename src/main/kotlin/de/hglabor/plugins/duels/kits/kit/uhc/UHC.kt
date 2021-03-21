package de.hglabor.plugins.duels.kits.kit.uhc

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.KitsGUI
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.kits.kit.cooldown.Classic
import de.hglabor.plugins.duels.kits.kit.trash.Sumo
import de.hglabor.plugins.duels.kits.specials.Specials
import net.axay.kspigot.items.itemStack
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class UHC : AbstractKit() {
    companion object {
        val INSTANCE = UHC()
    }

    override val name = "UHC"
    override val itemInGUI = Kits.guiItem(Material.GOLDEN_APPLE, name)
    override val arenaTag = ArenaTags.NONE
    override val type = null
    override val allowsRespawn = false
    override val category = KitCategory.UHC
    override val specials = setOf(Specials.ROD_KNOCKBACK)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.inventory.helmet = itemStack(Material.DIAMOND_HELMET) { addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)}
        player.inventory.chestplate = itemStack(Material.DIAMOND_CHESTPLATE) { addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)}
        player.inventory.leggings = itemStack(Material.IRON_LEGGINGS) { addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1)}
        player.inventory.boots = itemStack(Material.DIAMOND_BOOTS) { addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)}

        player.inventory.setItem(0, itemStack(Material.DIAMOND_SWORD) { addEnchantment(Enchantment.DAMAGE_ALL, 3) })
        player.inventory.setItem(1, ItemStack(Material.FISHING_ROD))
        player.inventory.setItem(2, itemStack(Material.BOW) { addEnchantment(Enchantment.ARROW_DAMAGE, 2) })
        player.inventory.setItem(3, ItemStack(Material.LAVA_BUCKET))
        player.inventory.setItem(4, ItemStack(Material.GOLDEN_APPLE, 7))
        player.inventory.setItem(5, KitUtils.goldenHead(4))
        player.inventory.setItem(6, ItemStack(Material.COBBLESTONE, 64))
        player.inventory.setItem(7, ItemStack(Material.WATER_BUCKET))
        player.inventory.setItem(8, ItemStack(Material.WATER_BUCKET))
        player.inventory.addItem(ItemStack(Material.ARROW, 24))
        player.inventory.addItem(ItemStack(Material.DIAMOND_AXE))
        player.inventory.addItem(ItemStack(Material.DIAMOND_PICKAXE))
        player.inventory.addItem(ItemStack(Material.OAK_PLANKS, 64))

        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    fun enable() {
        kits += INSTANCE
        Kits.mainKits += INSTANCE
    }
}

