package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.party.Party
import net.axay.kspigot.chat.KColors
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
    fun open(player: Player) {
        val gui = kSpigotGUI(GUIType.THREE_BY_NINE) {

            title = "${KColors.DODGERBLUE}Duels"

            page(1) {

                placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

                button(Slots.RowTwoSlotTwo, itemStack(Material.NETHERITE_SWORD) {
                    meta {
                        name = "split teamfight"
                    }
                }) {
                    val team = Party.get(player)!!
                    Duel.create(team.getSplitTeams().first, team.getSplitTeams().second, Kits.EHG)
                }
            }
        }
        player.openGUI(gui)
    }
}

