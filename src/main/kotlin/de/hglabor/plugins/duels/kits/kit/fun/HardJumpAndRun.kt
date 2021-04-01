package de.hglabor.plugins.duels.kits.kit.`fun`

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.KitCategory
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.specials.Specials
import net.axay.kspigot.chat.KColors
import org.bukkit.Material

object HardJumpAndRun: AbstractKit() {
    override val name = "${KColors.DARKRED}HARD ${KColors.DEEPSKYBLUE}Jump and Run"
    override val itemInGUI = Kits.guiItem(Material.NETHERITE_BOOTS, name)
    override val arenaTag = ArenaTags.HARDJUMPANDRUN
    override val allowsRespawn = true
    override val category = KitCategory.FUN
    override val specials = setOf(Specials.INVINICIBLE, Specials.JUMPANDRUN)
}