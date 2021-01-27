package de.hglabor.plugins.duels.kits.kit

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.kits.*
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class JumpAndRun : Kit(Kits.JUMPANDRUN) {
    override val name = "Jump and Run"
    override fun itemInGUIs() = Kits.guiItem(Material.DIAMOND_BOOTS, name, null)
    override val arenaTag = ArenaTags.JUMPANDRUN
    override val type = KitType.NONE
    override val specials = listOf(Specials.INVINICIBLE, Specials.JUMPANDRUN)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    override fun enable() {
        ChooseKitGUI.addContent(ChooseKitGUI.KitsGUICompoundElement(itemInGUIs()))
        kitMap[kits] = this
    }
}