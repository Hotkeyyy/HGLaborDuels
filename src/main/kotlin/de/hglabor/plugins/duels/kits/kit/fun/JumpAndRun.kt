package de.hglabor.plugins.duels.kits.kit.`fun`

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.KitCategory
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.specials.Specials
import org.bukkit.Material

object JumpAndRun: AbstractKit() {
    override val name = "Jump and Run"
    override val itemInGUI = Kits.guiItem(Material.DIAMOND_BOOTS, name)
    override val arenaTag = ArenaTags.JUMPANDRUN
    override val allowsRespawn = true
    override val isMainKit = true
    override val category = KitCategory.FUN
    override val specials = setOf(Specials.INVINICIBLE, Specials.JUMPANDRUN)
}