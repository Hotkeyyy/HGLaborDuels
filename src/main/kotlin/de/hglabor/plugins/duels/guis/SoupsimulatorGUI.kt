package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.soupsimulator.Soupsimulator
import de.hglabor.plugins.duels.soupsimulator.SoupsimulatorLevel
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

object SoupsimulatorGUI {
    fun guiBuilder(player: Player) = kSpigotGUI(GUIType.ONE_BY_NINE) {

        title = "${KColors.DODGERBLUE}Soupsimulator"

        page(1) {

            placeholder(Slots.All, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            button(Slots.RowOneSlotTwo, itemStack(Material.LIME_CONCRETE) {
                meta {
                    name = Localization.getMessage("soupsimulator.easy.name", player)
                    addLore {
                        +Localization.getMessage("soupsimulator.easy.lore", player)
                    }
                }
            }) {
                it.player.closeInventory()
                Soupsimulator(player).start(SoupsimulatorLevel.EASY)
            }

            button(Slots.RowOneSlotFour, itemStack(Material.YELLOW_CONCRETE) {
                meta {
                    name = Localization.getMessage("soupsimulator.medium.name", player)
                    addLore {
                        +Localization.getMessage("soupsimulator.medium.lore", player)
                    }
                }
            }) {
                it.player.closeInventory()
                Soupsimulator(player).start(SoupsimulatorLevel.MEDIUM)
            }

            button(Slots.RowOneSlotSix, itemStack(Material.RED_CONCRETE) {
                meta {
                    name = Localization.getMessage("soupsimulator.hard.name", player)
                    addLore {
                        +Localization.getMessage("soupsimulator.hard.lore.1", player)
                        +Localization.getMessage("soupsimulator.hard.lore.2", player)
                    }
                }
            }) {
                it.player.closeInventory()
                Soupsimulator(player).start(SoupsimulatorLevel.HARD)
            }

            button(Slots.RowOneSlotEight, itemStack(Material.PURPLE_CONCRETE) {
                meta {
                    name = Localization.getMessage("soupsimulator.bonus.name", player)
                    addLore {
                        +Localization.getMessage("soupsimulator.bonus.lore.1", player)
                        +Localization.getMessage("soupsimulator.bonus.lore.2", player)
                        +Localization.getMessage("soupsimulator.bonus.lore.3", player)
                    }
                }
            }) {
                it.player.closeInventory()
                Soupsimulator(player).start(SoupsimulatorLevel.BONUS)
            }
        }
    }
}