package de.hglabor.plugins.duels.kits.kit.soup

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.KitsGUI
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.kits.kit.`fun`.HardJumpAndRun
import de.hglabor.plugins.duels.kits.kit.cooldown.Classic
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class EHG : AbstractKit() {
    companion object {
        val INSTANCE = EHG()
    }

    override val name = "EHG"
    override val itemInGUI = Kits.guiItem(Material.STONE_SWORD, name)
    override val arenaTag = ArenaTags.NONE
    override val type = KitType.SOUP
    override val allowsRespawn = false
    override val category = KitCategory.SOUP
    override val specials = setOf(null)

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
        Kits.mainKits += INSTANCE
    }
}