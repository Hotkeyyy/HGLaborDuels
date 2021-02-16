package de.hglabor.plugins.duels.kits.kit

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.kits.Kit
import de.hglabor.plugins.duels.kits.KitType
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.kitMap
import de.hglabor.plugins.duels.kits.specials.Specials
import net.axay.kspigot.items.flag
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

class IceFishing : Kit(Kits.ICEFISHING) {
    override val name = "Ice Fishing"
    override val itemInGUIs = Kits.guiItem(Material.FISHING_ROD, name, "Pull your enemy into the water")
    override val arenaTag = ArenaTags.ICEFISHING
    override val type = KitType.NONE
    override val specials = listOf(Specials.NODAMAGE, Specials.DEADINWATER)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.inventory.setItem(0, itemStack(Material.FISHING_ROD) { amount = 1
            meta {isUnbreakable = true; flag(ItemFlag.HIDE_UNBREAKABLE) } })

        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    override fun enable() {
        kitMap[kits] = this
        ChooseKitGUI.addContent(ChooseKitGUI.KitsGUICompoundElement(itemInGUIs))
    }
}