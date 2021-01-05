package de.hglabor.plugins.duels.kits.kit

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.duel
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class NoDamage : Kit(Kits.NODAMAGE) {
    override val name = "NoDamage"
    override val itemInGUIs = Kits.guiItem(Material.SHIELD, name, null)
    override val arenaTag = ArenaTags.NONE
    override val type = KitType.NONE
    override val specials = listOf("nodamage")

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.inventory.addItem(KitUtils.sword(Material.DIAMOND_SWORD, true))

        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    override fun enable() {
        ChooseKitGUI.addContent(
            ChooseKitGUI.KitsGUICompoundElement(
                itemInGUIs,
                onClick = {
                    it.player.closeInventory()
                    Data.openedDuelGUI[it.player]?.let { it1 -> it.player.duel(it1, kits) }
                }
            ))
        kitMap[kits] = this
    }

}
