package de.hglabor.plugins.duels.kits.kit.`fun`

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.KitsGUI
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.kits.kit.cooldown.Classic
import de.hglabor.plugins.duels.kits.specials.Specials
import net.axay.kspigot.chat.KColors
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class HardJumpAndRun: AbstractKit() {
    companion object {
        val INSTANCE = HardJumpAndRun()
    }

    override val name = "${KColors.DARKRED}HARD ${KColors.DEEPSKYBLUE}Jump and Run"
    override val itemInGUI = Kits.guiItem(Material.NETHERITE_BOOTS, name)
    override val arenaTag = ArenaTags.HARDJUMPANDRUN
    override val type = null
    override val allowsRespawn = true
    override val category = KitCategory.FUN
    override val specials = setOf(Specials.INVINICIBLE, Specials.JUMPANDRUN)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    fun enable() {
        kits += INSTANCE

    }
}