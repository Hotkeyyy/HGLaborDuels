package de.hglabor.plugins.duels.guis.overview

import de.hglabor.plugins.duels.utils.Data
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.gui.*
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

object DuelOverviewGUI {
    fun open(player: Player, duelID: String) {
        val gui = kSpigotGUI(GUIType.THREE_BY_NINE) {

            title = "${KColors.DODGERBLUE}Duel §8| §7$duelID"

            page(1) {

                placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
                placeholder(Slots.RowTwoSlotTwo rectTo Slots.RowTwoSlotEight,
                    itemStack(Material.BLACK_STAINED_GLASS_PANE) { meta { name = null } })

                button(Slots.RowTwoSlotFour, itemStack(Material.LIGHT_BLUE_CONCRETE) {
                    meta {
                        name = "${KColors.DEEPSKYBLUE}Team 1"
                        addLore { + "§7Click to open information about Team 1" }
                    }
                }) {
                    DuelTeamOverviewGUI.open(player, duelID, Data.duelFromID[duelID]!!.teamOne as ArrayList<OfflinePlayer>)
                }

                button(Slots.RowTwoSlotSix, itemStack(Material.MAGENTA_CONCRETE) {
                    meta {
                        name = "${KColors.DEEPPINK}Team 2"
                        addLore { + "§7Click to open information about Team 2" }
                    }
                }) {
                    DuelTeamOverviewGUI.open(player, duelID, Data.duelFromID[duelID]!!.teamTwo as ArrayList<OfflinePlayer>)
                }
            }
        }
        player.openGUI(gui)
    }
}