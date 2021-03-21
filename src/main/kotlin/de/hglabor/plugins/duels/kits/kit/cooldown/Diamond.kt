package de.hglabor.plugins.duels.kits.kit.cooldown

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.kits.specials.Specials
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Diamond : AbstractKit() {
    companion object {
        val INSTANCE = Diamond()
    }

    override val name = "Diamond"
    override val itemInGUI = Kits.guiItem(Material.DIAMOND_CHESTPLATE, name)
    override val arenaTag = ArenaTags.NONE
    override val type = null
    override val allowsRespawn = false
    override val category = KitCategory.COOLDOWN
    override val specials = setOf(Specials.HITCOOLDOWN)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        KitUtils.armor(player, Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS)

        player.inventory.setItem(0, KitUtils.sword(Material.DIAMOND_SWORD, true))

        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 4.0
    }

    fun enable() {
        kits += INSTANCE
    }
}