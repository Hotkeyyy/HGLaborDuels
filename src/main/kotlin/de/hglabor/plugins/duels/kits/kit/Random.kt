package de.hglabor.plugins.duels.kits.kit

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.kits.Kit
import de.hglabor.plugins.duels.kits.KitType
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.kitMap
import org.bukkit.Material
import org.bukkit.entity.Player

class Random : Kit(Kits.RANDOM) {
    override val name = "Random"
    override val itemInGUIs = Kits.guiItem(Material.REPEATING_COMMAND_BLOCK, name, "Random Kit")
    override val arenaTag = ArenaTags.NONE
    override val type = KitType.NONE
    override val specials = listOf(null)

    override fun giveKit(player: Player) { return }

    override fun enable() {
        kitMap[kits] = this
        ChooseKitGUI.addContent(ChooseKitGUI.KitsGUICompoundElement(itemInGUIs))
    }
}