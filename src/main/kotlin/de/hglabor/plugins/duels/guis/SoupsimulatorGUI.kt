package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.soupsimulator.Soupsimulator
import de.hglabor.plugins.duels.soupsimulator.SoupsimulatorLevel
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
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
import org.bukkit.entity.Player

object SoupsimulatorGUI {
    fun open(player: Player) {
        val gui = kSpigotGUI(GUIType.ONE_BY_NINE) {

            title = "${KColors.DODGERBLUE}Soupsimulator"

            page(1) {

                placeholder(Slots.All, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

                button(Slots.RowOneSlotTwo, itemStack(Material.LIME_CONCRETE) {
                    meta {
                        name = if (player.localization("de"))
                            Localization.SOUPSIMULATOR_GUI_EASY_NAME_DE
                        else
                            Localization.SOUPSIMULATOR_GUI_EASY_NAME_EN
                        addLore {
                            if (player.localization("de")) {
                                +Localization.SOUPSIMULATOR_GUI_EASY_LORE1_DE
                                +Localization.SOUPSIMULATOR_GUI_EASY_LORE2_DE
                            } else {
                                +Localization.SOUPSIMULATOR_GUI_EASY_LORE1_EN
                                +Localization.SOUPSIMULATOR_GUI_EASY_LORE2_EN
                            }
                        }
                    }
                }) {
                    it.player.closeInventory()
                    Soupsimulator(player).start(SoupsimulatorLevel.EASY)
                }

                button(Slots.RowOneSlotFour, itemStack(Material.YELLOW_CONCRETE) {
                    meta {
                        name = if (player.localization("de"))
                            Localization.SOUPSIMULATOR_GUI_MEDIUM_NAME_DE
                        else
                            Localization.SOUPSIMULATOR_GUI_MEDIUM_NAME_EN
                        addLore {
                            if (player.localization("de"))
                                +Localization.SOUPSIMULATOR_GUI_MEDIUM_LORE1_DE
                            else
                                +Localization.SOUPSIMULATOR_GUI_MEDIUM_LORE1_EN
                        }
                    }
                }) {
                    it.player.closeInventory()
                    Soupsimulator(player).start(SoupsimulatorLevel.MEDIUM)
                }

                button(Slots.RowOneSlotSix, itemStack(Material.RED_CONCRETE) {
                    meta {
                        name = if (player.localization("de"))
                        Localization.SOUPSIMULATOR_GUI_HARD_NAME_DE
                    else
                        Localization.SOUPSIMULATOR_GUI_HARD_NAME_EN
                        addLore {
                            if (player.localization("de"))
                                +Localization.SOUPSIMULATOR_GUI_HARD_LORE1_DE
                            else
                                +Localization.SOUPSIMULATOR_GUI_HARD_LORE1_EN
                        }
                    }
                }) {
                    it.player.closeInventory()
                    Soupsimulator(player).start(SoupsimulatorLevel.HARD)
                }

                button(Slots.RowOneSlotEight, itemStack(Material.PURPLE_CONCRETE) {
                    meta {
                        name = Localization.SOUPSIMULATOR_GUI_BONUS_NAME
                        addLore {
                            if (player.localization("de")) {
                                +Localization.SOUPSIMULATOR_GUI_BONUS_LORE1_DE
                                +Localization.SOUPSIMULATOR_GUI_BONUS_LORE2_DE
                            } else {
                                +Localization.SOUPSIMULATOR_GUI_BONUS_LORE1_EN
                                +Localization.SOUPSIMULATOR_GUI_BONUS_LORE2_EN
                            }
                        }
                    }
                }) {
                    it.player.closeInventory()
                    Soupsimulator(player).start(SoupsimulatorLevel.BONUS)
                }
            }
        }
        player.openGUI(gui)
    }
}