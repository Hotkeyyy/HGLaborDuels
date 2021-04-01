package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.Data
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Material
import org.bukkit.entity.Player

object PartyGameGUI {
    fun guiBuilder(player: Player) = kSpigotGUI(GUIType.THREE_BY_NINE) {

        title = Localization.getMessage("party.games.gui.title", player)

        page(1) {

            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            button(Slots.RowTwoSlotTwo, itemStack(Material.NETHERITE_SWORD) {
                meta {
                    name = Localization.getMessage("party.games.splitTeamfight.name", player)
                }
            }) {
                Data.openedKitInventory[player] = KitsGUI.KitInventories.SPLITPARTY
                player.openGUI(KitsGUI.guiBuilder())
            }
        }
    }
}

