package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.data.PlayerSettings
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.localization.sendMsg
import de.hglabor.plugins.duels.utils.Data
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player

object PlayerSettingsGUI {

    fun guiBuilder(player: Player) = kSpigotGUI(GUIType.THREE_BY_NINE) {

        title = Localization.getMessage("settingsgui.title", player)

        page(1) {

            placeholder(Slots.All, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            button(Slots.RowTwoSlotTwo, knockbackItem(player)) {
                val settings = PlayerSettings.get(player)
                settings.setKnockback(settings.knockback().next())
                it.guiInstance[Slots.RowTwoSlotTwo] = knockbackItem(player)
                player.playSound(player.location, Sound.UI_BUTTON_CLICK, 5f, 1f)
            }

            button(Slots.RowTwoSlotFour, allowSpectatorsItem(player)) {
                val settings = PlayerSettings.get(player)
                settings.setAllowSpectators(settings.allowSpectators().next())
                it.guiInstance[Slots.RowTwoSlotFour] = allowSpectatorsItem(player)
                player.playSound(player.location, Sound.UI_BUTTON_CLICK, 5f, 1f)
            }

            button(Slots.RowTwoSlotSix, itemStack(Material.CHEST) {
                meta {
                    name = Localization.getMessage("settingsgui.item.inventorysorting.name", player)
                }
            }) {
                Data.openedKitInventory[player] = KitsGUI.KitInventories.INVENTORYSORTING
                player.openGUI(KitsGUI.guiBuilder())
                player.sendMsg("inventorysort.warn.inventoryWillFill.1")
                player.sendMsg("inventorysort.warn.inventoryWillFill.2")
            }
        }
    }

    private fun knockbackItem(player: Player) = itemStack(Material.PISTON) {
        meta {
            name = Localization.getMessage("settingsgui.item.knockback.name", player)

            val settings = PlayerSettings.get(player)
            addLore {
                +"${KColors.MEDIUMPURPLE}${settings.knockback().version}"
                +"${KColors.DIMGRAY}${settings.knockback().next().version}"
            }
        }
    }

    private fun allowSpectatorsItem(player: Player) = itemStack(Material.ENDER_EYE) {
        meta {
            name = Localization.getMessage("settingsgui.item.allowSpecs.name", player)

            val settings = PlayerSettings.get(player)
            addLore {
                +"${KColors.MEDIUMPURPLE}${Localization.getMessage(settings.allowSpectators().key, player)}"
                +"${KColors.DIMGRAY}${Localization.getMessage(settings.allowSpectators().next().key, player)}"
            }
        }
    }

    inline fun <reified T : Enum<T>> T.next(): T {
        val values = enumValues<T>()
        val nextOrdinal = (ordinal + 1) % values.size
        return values[nextOrdinal]
    }
}


