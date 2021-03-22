package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.data.PlayerSettings
import de.hglabor.plugins.duels.localization.Localization
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Material
import org.bukkit.entity.Player

object ChooseKnockbackGUI {

    fun guiBuilder(player: Player) = kSpigotGUI(GUIType.FOUR_BY_NINE) {

        title = Localization.INSTANCE.getMessage("chooseknockbackgui.title", player)

        page(1) {

            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            button(Slots.RowTwoSlotThree, itemStack(Material.LIGHT_BLUE_CONCRETE) {
                amount = 8
                meta {
                    name = "${KColors.DEEPSKYBLUE}1.8 Knockback"
                    addLore {
                        + Localization.INSTANCE.getMessage("chooseknockbackgui.lore", mutableMapOf("version" to "1.8"), player)
                    }
                }
            }) {
                player.closeInventory()
                val settings = PlayerSettings.get(player)
                settings.setKnockback(PlayerSettings.Companion.Knockback.OLD)
            }

            button(Slots.RowTwoSlotThree, itemStack(Material.MAGENTA_CONCRETE) {
                amount = 16
                meta {
                    name = "${KColors.DEEPPINK}1.16 Knockback"
                    addLore {
                        + Localization.INSTANCE.getMessage("chooseknockbackgui.lore", mutableMapOf("version" to "1.16"), player)
                    }
                }
            }) {
                player.closeInventory()
                val settings = PlayerSettings.get(player)
                settings.setKnockback(PlayerSettings.Companion.Knockback.NEW)
            }
        }
    }
}