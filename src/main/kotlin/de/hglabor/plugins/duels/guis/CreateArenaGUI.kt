package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.arenas.arenaFromPlayer
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.reset
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.chat.input.awaitAnvilInput
import net.axay.kspigot.event.listen
import net.axay.kspigot.gui.*
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.utils.mark
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

object CreateArenaGUI {

    fun openCreateArenaGUI(player: Player) {
        val arena = arenaFromPlayer[player]
        player.openGUI(kSpigotGUI(GUIType.THREE_BY_NINE) {

            title = if (player.localization("de"))
                Localization.ARENA_CREATION_GUI_NAME_DE
            else
                Localization.ARENA_CREATION_GUI_NAME_EN

            page(1) {

                placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

                placeholder(
                    Slots.RowOneSlotTwo rectTo Slots.RowThreeSlotEight,
                    itemStack(Material.BLACK_STAINED_GLASS_PANE) { meta { name = null } }
                )

                button(Slots.RowTwoSlotFive, itemStack(Material.NAME_TAG) {
                    meta {
                        name = if (player.localization("de"))
                            Localization.ARENA_CREATION_GUI_SETNAMEITEM_NAME_DE
                        else
                            Localization.ARENA_CREATION_GUI_SETNAMEITEM_NAME_EN

                        if (arena?.name != "") {
                            addLore {
                                +"${KColors.CORNSILK}${arena?.name}"
                            }
                        }
                    }
                }) {
                    player.awaitAnvilInput("Gib den Arenanamen ein!") { servername ->
                        val name = servername.input ?: kotlin.run {
                            player.sendMessage("${KColors.RED}ABBRUCH. ${KColors.INDIANRED}Du musst einen gültigen Namen angeben!")
                            return@awaitAnvilInput
                        }
                        arena!!.name(name)
                    }
                }

                button(Slots.RowTwoSlotThree, itemStack(Material.NETHERITE_PICKAXE) {
                    meta {
                        name = if (player.localization("de"))
                            Localization.ARENA_CREATION_GUI_CORNERITEM_NAME_DE
                        else
                            Localization.ARENA_CREATION_GUI_CORNERITEM_NAME_EN
                        addLore {
                            if (player.localization("de")) {
                                +Localization.ARENA_CREATION_GUI_LORE_GETTOOL_DE
                                if (arena?.corner1 != null)
                                    +Localization.ARENA_CREATION_GUI_LORE_POS1_DE
                                if (arena?.corner2 != null)
                                    +Localization.ARENA_CREATION_GUI_LORE_POS2_DE

                            } else {
                                +Localization.ARENA_CREATION_GUI_LORE_GETTOOL_EN
                                if (arena?.corner1 != null)
                                    +Localization.ARENA_CREATION_GUI_LORE_POS1_EN
                                if (arena?.corner2 != null)
                                    +Localization.ARENA_CREATION_GUI_LORE_POS2_EN
                            }
                        }
                    }
                }) {
                    it.player.closeInventory()
                    it.player.inventory.setItem(4, cornerItem(player))
                }

                button(Slots.RowTwoSlotSeven, itemStack(Material.NETHERITE_SHOVEL) {
                    meta {
                        name = if (player.localization("de"))
                            Localization.ARENA_CREATION_GUI_SPAWNITEM_NAME_DE
                        else
                            Localization.ARENA_CREATION_GUI_SPAWNITEM_NAME_EN
                        addLore {
                            if (player.localization("de")) {
                                +Localization.ARENA_CREATION_GUI_LORE_GETTOOL_DE
                                if (arena?.corner1 != null)
                                    +Localization.ARENA_CREATION_GUI_LORE_POS1_DE
                                if (arena?.corner2 != null)
                                    +Localization.ARENA_CREATION_GUI_LORE_POS2_DE

                            } else {
                                +Localization.ARENA_CREATION_GUI_LORE_GETTOOL_EN
                                if (arena?.corner1 != null)
                                    +Localization.ARENA_CREATION_GUI_LORE_POS1_EN
                                if (arena?.corner2 != null)
                                    +Localization.ARENA_CREATION_GUI_LORE_POS2_EN
                            }
                        }
                    }
                }) {
                    it.player.closeInventory()
                    it.player.inventory.setItem(4, spawnItem(player))
                }

                button(Slots.RowThreeSlotFive, itemStack(Material.PAPER) {
                    meta {
                        name = if (player.localization("de"))
                            Localization.ARENA_CREATION_GUI_SETTAGITEM_NAME_DE
                        else
                            Localization.ARENA_CREATION_GUI_SETTAGITEM_NAME_EN
                        addLore {
                            if (arena?.tag != null) {
                                addLore {
                                    +"${KColors.CORNSILK}${arena.tag}"
                                }
                            }
                        }
                    }
                }) {
                    it.player.openGUI(ArenaTagsGUI.gui)
                }

                button(Slots.RowOneSlotFour, itemStack(Material.SCUTE) {
                    meta {
                        name = if (player.localization("de"))
                            Localization.ARENA_CREATION_GUI_SAVEITEM_NAME_DE
                        else
                            Localization.ARENA_CREATION_GUI_SAVEITEM_NAME_EN
                    }
                }) {
                    it.player.closeInventory()
                    arena?.save()
                    player.reset()
                }

                button(Slots.RowOneSlotSix, itemStack(Material.BARRIER) {
                    meta {
                        name = if (player.localization("de"))
                            Localization.ARENA_CREATION_GUI_ABORTITEM_NAME_DE
                        else
                            Localization.ARENA_CREATION_GUI_ABORTITEM_NAME_EN
                        addLore {
                            if (player.localization("de"))
                                +Localization.ARENA_CREATION_GUI_ABORTITEM_LORE_DE
                            else
                                +Localization.ARENA_CREATION_GUI_ABORTITEM_LORE_EN
                        }
                    }
                }) {
                    arenaFromPlayer.remove(player)
                    if (player.localization("de"))
                        player.sendMessage(Localization.ARENA_CREATION_ABORTED_DE)
                    else
                        player.sendMessage(Localization.ARENA_CREATION_ABORTED_EN)
                    player.reset()
                }
            }
        })
    }

    fun cornerItem(player: Player): ItemStack {
        val cornerItem = itemStack(Material.NETHERITE_PICKAXE) {
            meta {
                name = if (player.localization("de"))
                    Localization.ARENA_CREATION_GUI_CORNERITEM_NAME_DE
                else
                    Localization.ARENA_CREATION_GUI_CORNERITEM_NAME_EN
                addLore {
                    if (player.localization("de")) {
                        +Localization.ARENA_CREATION_CORNERITEM_LORE1_DE
                        +Localization.ARENA_CREATION_CORNERITEM_LORE2_DE
                    } else {
                        +Localization.ARENA_CREATION_CORNERITEM_LORE1_EN
                        +Localization.ARENA_CREATION_CORNERITEM_LORE2_EN
                    }
                }
            }
            mark("corner")
            amount = 1
        }
        return cornerItem
    }

    fun spawnItem(player: Player): ItemStack {
        return itemStack(Material.NETHERITE_SHOVEL) {
            meta {
                name = if (player.localization("de"))
                    Localization.ARENA_CREATION_GUI_SPAWNITEM_NAME_DE
                else
                    Localization.ARENA_CREATION_GUI_SPAWNITEM_NAME_EN
                addLore {
                    if (player.localization("de")) {
                        +Localization.ARENA_CREATION_SPAWNITEM_LORE1_DE
                        +Localization.ARENA_CREATION_SPAWNITEM_LORE2_DE
                    } else {
                        +Localization.ARENA_CREATION_SPAWNITEM_LORE1_EN
                        +Localization.ARENA_CREATION_SPAWNITEM_LORE2_EN
                    }
                }
            }
            mark("spawn")
            amount = 1
        }
    }


    fun enable() {
        listen<InventoryCloseEvent> {
            if (it.view.title == "${KColors.DODGERBLUE}Duels") {
                Data.openedDuelGUI.remove(it.player)
            }
        }
    }
}