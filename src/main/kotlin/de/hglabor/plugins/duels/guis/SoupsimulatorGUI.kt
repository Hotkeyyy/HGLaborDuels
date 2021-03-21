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
    fun guiBuilder(player: Player) = kSpigotGUI(GUIType.ONE_BY_NINE) {

        title = "${KColors.DODGERBLUE}Soupsimulator"

        page(1) {

            placeholder(Slots.All, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            button(Slots.RowOneSlotTwo, itemStack(Material.LIME_CONCRETE) {
                meta {
                    name = Localization.INSTANCE.getMessage("soupsimulator.easy.name", player)
                    addLore {
                        +Localization.INSTANCE.getMessage("soupsimulator.easy.lore", player)
                    }
                }
            }) {
                it.player.closeInventory()
                Soupsimulator(player).start(SoupsimulatorLevel.EASY)
            }

            button(Slots.RowOneSlotFour, itemStack(Material.YELLOW_CONCRETE) {
                meta {
                    name = Localization.INSTANCE.getMessage("soupsimulator.medium.name", player)
                    addLore {
                        +Localization.INSTANCE.getMessage("soupsimulator.medium.lore", player)
                    }
                }
            }) {
                it.player.closeInventory()
                Soupsimulator(player).start(SoupsimulatorLevel.MEDIUM)
            }

            button(Slots.RowOneSlotSix, itemStack(Material.RED_CONCRETE) {
                meta {
                    name = Localization.INSTANCE.getMessage("soupsimulator.hard.name", player)
                    addLore {
                        +Localization.INSTANCE.getMessage("soupsimulator.hard.lore", player)
                    }
                }
            }) {
                it.player.closeInventory()
                Soupsimulator(player).start(SoupsimulatorLevel.HARD)
            }

            button(Slots.RowOneSlotEight, itemStack(Material.PURPLE_CONCRETE) {
                meta {
                    name = Localization.INSTANCE.getMessage("soupsimulator.bonus.name", player)
                    addLore {
                        +Localization.INSTANCE.getMessage("soupsimulator.bonus.lore", player)
                    }
                }
            }) {
                it.player.closeInventory()
                Soupsimulator(player).start(SoupsimulatorLevel.BONUS)
            }
        }
    }
}