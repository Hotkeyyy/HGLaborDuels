package de.hglabor.plugins.duels.kits.kit

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.kits.Kit
import de.hglabor.plugins.duels.kits.KitType
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.kitMap
import de.hglabor.plugins.duels.kits.specials.Specials
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class Sumo : Kit(Kits.SUMO) {
    override val name = "Sumo"
    override val itemInGUIs = Kits.guiItem(Material.LEAD, name, null)
    override val arenaTag = ArenaTags.SUMO
    override val type = KitType.NONE
    override val specials = listOf(Specials.NODAMAGE, Specials.DEADINWATER)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    override fun enable() {
        kitMap[kits] = this
        ChooseKitGUI.addContent(ChooseKitGUI.KitsGUICompoundElement(itemInGUIs))
    }
}