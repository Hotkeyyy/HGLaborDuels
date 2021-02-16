package de.hglabor.plugins.duels.kits.kit

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.kits.specials.Specials
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class Ninja : Kit(Kits.NINJA) {
    override val name = "Ninja"
    override val itemInGUIs = Kits.guiItem(Material.INK_SAC, name, "Soup")
    override val arenaTag = ArenaTags.NONE
    override val type = KitType.SOUP
    override val specials = listOf(Specials.NINJA)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.inventory.setItem(0, KitUtils.sword(Material.STONE_SWORD, false))

        KitUtils.giveRecraft(player, 32)
        KitUtils.giveSoups(player)

        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    override fun enable() {
        kitMap[kits] = this
        ChooseKitGUI.addContent(ChooseKitGUI.KitsGUICompoundElement(itemInGUIs))
    }
}
