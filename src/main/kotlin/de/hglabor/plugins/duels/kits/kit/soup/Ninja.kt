package de.hglabor.plugins.duels.kits.kit.soup

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.KitsGUI
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.kits.kit.`fun`.HardJumpAndRun
import de.hglabor.plugins.duels.kits.kit.cooldown.Classic
import de.hglabor.plugins.duels.kits.specials.Specials
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class Ninja : AbstractKit() {
    companion object {
        val INSTANCE = Ninja()
    }

    override val name = "Ninja"
    override val itemInGUI = Kits.guiItem(Material.INK_SAC, name)
    override val arenaTag = ArenaTags.NONE
    override val type = KitType.SOUP
    override val allowsRespawn = false
    override val category = KitCategory.SOUP
    override val specials = setOf(Specials.NINJA)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.inventory.setItem(0, KitUtils.sword(Material.STONE_SWORD, false))

        KitUtils.giveRecraft(player, 32)
        KitUtils.fillEmptySlotsWithSoup(player)

        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    fun enable() {
        kits += INSTANCE

    }
}
