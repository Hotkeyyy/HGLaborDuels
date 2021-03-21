package de.hglabor.plugins.duels.kits.kit.trash

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.KitsGUI
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.kits.kit.`fun`.HardJumpAndRun
import de.hglabor.plugins.duels.kits.kit.cooldown.Classic
import de.hglabor.plugins.duels.kits.specials.Specials
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class Sumo : AbstractKit() {
    companion object {
        val INSTANCE = Sumo()
    }

    override val name = "Sumo"
    override val itemInGUI = Kits.guiItem(Material.LEAD, name)
    override val arenaTag = ArenaTags.SUMO
    override val type = null
    override val allowsRespawn = false
    override val category = KitCategory.TRASH
    override val specials = setOf(Specials.NODAMAGE, Specials.DEADINWATER)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    fun enable() {
        kits += INSTANCE
        Kits.mainKits += INSTANCE
    }
}