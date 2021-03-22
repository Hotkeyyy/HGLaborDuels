package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.arenas.arenaFromPlayer
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.localization.sendMsg
import de.hglabor.plugins.duels.utils.Data
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

    fun guiBuilder(player: Player): GUI<ForInventoryThreeByNine> {
        val arena = arenaFromPlayer[player]
        return kSpigotGUI(GUIType.THREE_BY_NINE) {

            title = Localization.INSTANCE.getMessage("createArenaGUI.name", player)

            page(1) {

                placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

                placeholder(
                    Slots.RowOneSlotTwo rectTo Slots.RowThreeSlotEight,
                    itemStack(Material.BLACK_STAINED_GLASS_PANE) { meta { name = null } }
                )

                button(Slots.RowTwoSlotFive, itemStack(Material.NAME_TAG) {
                    meta {
                        name = Localization.INSTANCE.getMessage("createArenaGUI.item.setName.name", player)

                        if (arena?.name != "") {
                            addLore {
                                +"${KColors.CORNSILK}${arena?.name}"
                            }
                        }
                    }
                }) {
                    player.awaitAnvilInput(Localization.INSTANCE.getMessage("createArenaGUI.nameInput.title", player)) { servername ->
                        val name = servername.input ?: kotlin.run {
                            player.sendMsg("createArenaGUI.nameInput.abort")
                            return@awaitAnvilInput
                        }
                        arena!!.name(name)
                    }
                }

                button(Slots.RowTwoSlotThree, itemStack(Material.NETHERITE_PICKAXE) {
                    meta {
                        name = Localization.INSTANCE.getMessage("createArenaGUI.item.setCorner.name", player)
                        addLore {
                            +Localization.INSTANCE.getMessage("createArenaGUI.item.lore.getTool", player)
                            if (arena?.corner1 != null)
                                +Localization.INSTANCE.getMessage("createArenaGUI.item.lore.pos", mutableMapOf("n" to "1"), player)
                            if (arena?.corner2 != null)
                                +Localization.INSTANCE.getMessage("createArenaGUI.item.lore.pos", mutableMapOf("n" to "2"), player)
                        }
                    }
                }) {
                    it.player.closeInventory()
                    it.player.inventory.setItem(4, cornerItem(player))
                }

                button(Slots.RowTwoSlotSeven, itemStack(Material.NETHERITE_SHOVEL) {
                    meta {
                        name = Localization.INSTANCE.getMessage("createArenaGUI.item.setSpawn.name", player)
                        addLore {
                            +Localization.INSTANCE.getMessage("createArenaGUI.item.lore.getTool", player)
                            if (arena?.corner1 != null)
                                +Localization.INSTANCE.getMessage("createArenaGUI.item.lore.pos", mutableMapOf("n" to "1"), player)
                            if (arena?.corner2 != null)
                                +Localization.INSTANCE.getMessage("createArenaGUI.item.lore.pos", mutableMapOf("n" to "2"), player)
                        }
                    }
                }) {
                    it.player.closeInventory()
                    it.player.inventory.setItem(4, spawnItem(player))
                }

                button(Slots.RowThreeSlotFive, itemStack(Material.PAPER) {
                    meta {
                        name = Localization.INSTANCE.getMessage("createArenaGUI.item.setTag.name", player)
                        addLore {
                            addLore {
                                +"${KColors.CORNSILK}${arena?.tag}"
                            }
                        }
                    }
                }) {
                    it.player.openGUI(ArenaTagsGUI.gui)
                }

                button(Slots.RowOneSlotFour, itemStack(Material.SCUTE) {
                    meta {
                        name = Localization.INSTANCE.getMessage("createArenaGUI.item.save.name", player)
                    }
                }) {
                    it.player.closeInventory()
                    arena?.save()
                    player.reset()
                }

                button(Slots.RowOneSlotSix, itemStack(Material.BARRIER) {
                    meta {
                        name = Localization.INSTANCE.getMessage("createArenaGUI.item.abort.name", player)
                        addLore {
                            +Localization.INSTANCE.getMessage("createArenaGUI.item.abort.lore", player)
                        }
                    }
                }) {
                    arenaFromPlayer.remove(player)
                    player.sendMsg("createArenaGUI.message.aborted")
                    player.reset()
                }
            }
        }
    }

    private fun cornerItem(player: Player): ItemStack {
        return itemStack(Material.NETHERITE_PICKAXE) {
            meta {
                name = Localization.INSTANCE.getMessage("createArenaGUI.item.setCorner.name", player)
                addLore {
                    + Localization.INSTANCE.getMessage("createArenaGUI.itemInInventory.corner.lore.leftClick", player)
                    + Localization.INSTANCE.getMessage("createArenaGUI.itemInInventory.corner.lore.rightClick", player)
                }
            }
            mark("corner")
            amount = 1
        }
    }

    private fun spawnItem(player: Player): ItemStack {
        return itemStack(Material.NETHERITE_SHOVEL) {
            meta {
                name = Localization.INSTANCE.getMessage("createArenaGUI.item.setName.name", player)
                addLore {
                    + Localization.INSTANCE.getMessage("createArenaGUI.itemInInventory.spawn.lore.leftClick", player)
                    + Localization.INSTANCE.getMessage("createArenaGUI.itemInInventory.spawn.lore.rightClick", player)
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