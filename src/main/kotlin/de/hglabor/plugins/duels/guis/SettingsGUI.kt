package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.settings.Settings
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.gui.*
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

object SettingsGUI {
    fun openGUI(player: Player) {
        player.openGUI(kSpigotGUI(GUIType.THREE_BY_NINE) {
            title = if (player.localization("de"))
                Localization.SETTINGSGUI_NAME_DE
            else
                Localization.SETTINGSGUI_NAME_EN

            page(1) {

                placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

                placeholder(
                    Slots.RowOneSlotTwo rectTo Slots.RowThreeSlotEight,
                    itemStack(Material.BLACK_STAINED_GLASS_PANE) { meta { name = null } })

                button(Slots.RowTwoSlotFour, itemStack(Material.PISTON) {
                    meta {
                        name = if (player.localization("de")) Localization.SETTINGSGUI_KNOCKBACK_NAME_DE
                        else Localization.SETTINGSGUI_KNOCKBACK_NAME_EN

                        addLore {
                            if (Settings.knockback[player.uniqueId] == Settings.Knockback.OLD) {
                                +"${KColors.MEDIUMPURPLE}1.8"
                                +"${KColors.GRAY}1.16"
                            } else {
                                +"${KColors.MEDIUMPURPLE}1.16"
                                +"${KColors.GRAY}1.8"
                            }
                        }
                    }
                }) {
                    if (Settings.knockback[player.uniqueId] == Settings.Knockback.OLD)
                        Settings.knockback[player.uniqueId] = Settings.Knockback.NEW
                    else
                        Settings.knockback[player.uniqueId] = Settings.Knockback.OLD
                    player.playSound(player.location, Sound.UI_BUTTON_CLICK, 3f, 3f)
                    openGUI(player)
                }
                Settings.hitSound[player.uniqueId] = !Settings.hitSound[player.uniqueId]!!
                player.playSound(player.location, Sound.UI_BUTTON_CLICK, 3f, 3f)



                button(Slots.RowTwoSlotSix, itemStack(Material.NOTE_BLOCK) {
                    meta {
                        name = if (player.localization("de"))
                            Localization.SETTINGSGUI_HITSOUND_NAME_DE
                        else
                            Localization.SETTINGSGUI_HITSOUND_NAME_EN
                        addLore {
                            if (player.localization("de")) {
                                if (Settings.hitSound[player.uniqueId]!!) {
                                    +"${KColors.MEDIUMPURPLE}An"
                                    +"${KColors.GRAY}Aus"
                                } else {
                                    +"${KColors.MEDIUMPURPLE}Aus"
                                    +"${KColors.GRAY}An"
                                }
                            } else {
                                if (Settings.hitSound[player.uniqueId]!!) {
                                    +"${KColors.MEDIUMPURPLE}On"
                                    +"${KColors.GRAY}Off"
                                } else {
                                    +"${KColors.MEDIUMPURPLE}Off"
                                    +"${KColors.GRAY}On"
                                }
                            }
                        }

                    }
                }) {
                    Settings.hitSound[player.uniqueId] = Settings.hitSound[player.uniqueId] != true
                    player.playSound(player.location, Sound.UI_BUTTON_CLICK, 3f, 3f)
                    openGUI(player)
                }
            }
        })
    }
}