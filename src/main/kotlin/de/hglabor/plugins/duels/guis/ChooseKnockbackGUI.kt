package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.data.PlayerSettings
import de.hglabor.plugins.duels.localization.Localization
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.gui.linTo
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Material
import org.bukkit.entity.Player

object ChooseKnockbackGUI {

    fun guiBuilder(player: Player) = kSpigotGUI(GUIType.THREE_BY_NINE) {
        val settings = PlayerSettings.get(player)
        title = Localization.getMessage("chooseknockbackgui.title", player)

        page(1) {

            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
            placeholder(Slots.RowTwoSlotTwo linTo Slots.RowTwoSlotEight,
                itemStack(Material.BLACK_STAINED_GLASS_PANE) { meta { name = null } })
            button(Slots.RowTwoSlotThree, itemStack(Material.LIGHT_BLUE_CONCRETE) {
                amount = 8
                meta {
                    name = "${KColors.DEEPSKYBLUE}1.8 Knockback"
                }
            }) {
                player.closeInventory()
                settings.setKnockback(PlayerSettings.Companion.Knockback.OLD)
            }

            placeholder(Slots.RowTwoSlotFive, itemStack(Material.PAPER) {
                meta {
                    name = Localization.getMessage("chooseknockbackgui.information.name", player)
                    addLore {
                        + Localization.getMessage("chooseknockbackgui.information.lore.1", player)
                        + ""
                        + ""
                        + Localization.getMessage("chooseknockbackgui.information.lore.2", player)
                        + Localization.getMessage("chooseknockbackgui.information.lore.3", player)
                    }
                }
            })

            button(Slots.RowTwoSlotSeven, itemStack(Material.MAGENTA_CONCRETE) {
                amount = 16
                meta {
                    name = "${KColors.DEEPPINK}1.16 Knockback"
                }
            }) {
                player.closeInventory()
                settings.setKnockback(PlayerSettings.Companion.Knockback.NEW)
            }
        }
    }
}