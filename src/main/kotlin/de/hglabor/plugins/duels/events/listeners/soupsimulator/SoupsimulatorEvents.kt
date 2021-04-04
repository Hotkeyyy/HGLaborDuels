package de.hglabor.plugins.duels.events.listeners.soupsimulator

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.database.data.DataHolder
import de.hglabor.plugins.duels.duel.GameState
import de.hglabor.plugins.duels.guis.SoupsimulatorGUI
import de.hglabor.plugins.duels.utils.sendMsg
import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.soupsimulator.*
import de.hglabor.plugins.duels.utils.Data
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.getHandItem
import net.axay.kspigot.extensions.events.isRightClick
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.runnables.async
import net.axay.kspigot.utils.hasMark
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent

object SoupsimulatorEvents {
    init {
        listen<PlayerDropItemEvent> {
            val player = it.player
            val duelsPlayer = DuelsPlayer.get(player)
            if (duelsPlayer.isInSoupsimulator()) {
                it.isCancelled = it.itemDrop.itemStack.type == Material.BOWL
                Data.droppedItemInSoupsimulator.add(it.itemDrop)
            }
        }

        listen<EntitySpawnEvent> {
            if (it.entityType == EntityType.DROPPED_ITEM) {
                if (Data.droppedItemInSoupsimulator.contains(it.entity)) {
                    it.isCancelled = true
                    Data.droppedItemInSoupsimulator.remove(it.entity)
                }
            }
        }

        listen<PlayerItemHeldEvent> {
            val player = it.player
            val duelsPlayer = DuelsPlayer.get(player)

            if (duelsPlayer.isInSoupsimulator()) {
                val simulator = Soupsimulator.get(player) ?: return@listen
                if (simulator.state != GameState.INGAME) return@listen

                if (simulator.Task == SoupsimulatorTasks.SOUP) {
                    val newItemType = player.inventory.getItem(it.newSlot)?.type ?: Material.AIR
                    if (newItemType == Material.STONE_SWORD) {
                        var playerHasSoups = false
                        for (item in player.inventory.contents) {
                            if (item.type != Material.AIR && item.type != Material.STONE_SWORD) {
                                playerHasSoups = true
                                break
                            }
                        }
                        if (!playerHasSoups) {
                            simulator.score += 1
                            simulator.nextTask()
                        }
                    } else {
                        if (player.inventory.getItem(it.newSlot)?.type == Material.MUSHROOM_STEW || player.inventory.getItem(
                                it.newSlot
                            )?.type == Material.BOWL
                        )
                            return@listen

                        simulator.wrongHotkeys += 1
                        if (simulator.level == SoupsimulatorLevel.EASY) {
                            player.damage(0.0)
                        } else {
                            if (player.health - 4 <= 0) {
                                player.sendMsg("soupsimulator.finish.failed")
                                simulator.end()
                            } else {
                                player.damage(4.0)
                            }
                        }
                    }
                }
            }
        }

        listen<CraftItemEvent> {
            val player: Player = it.whoClicked as Player
            val duelsPlayer = DuelsPlayer.get(player)
            if (duelsPlayer.isInSoupsimulator()) {
                val simulator = Soupsimulator.get(player) ?: return@listen
                if (simulator.state == GameState.INGAME)
                    if (simulator.Task == SoupsimulatorTasks.RECRAFT) {
                        if (it.currentItem?.type == Material.MUSHROOM_STEW && it.isShiftClick) {
                            simulator.score += 3
                            simulator.recrafts += 1
                            player.closeInventory()
                            async {
                                simulator.sendRecraftRefillTime()
                                simulator.nextTask()
                            }
                        }
                    }
            }
        }

        listen<InventoryClickEvent> {
            val player: Player = it.whoClicked as Player
            val duelsPlayer = DuelsPlayer.get(player)

            if (!duelsPlayer.isInSoupsimulator()) {
                return@listen
            }
            val simulator = Soupsimulator.get(player) ?: return@listen
            if (simulator.state == GameState.INGAME)
                if (simulator.Task == SoupsimulatorTasks.REFILL) {
                    var soupsInHotbar = 0
                    if (it.currentItem?.type == Material.MUSHROOM_STEW && it.isShiftClick) {
                        Bukkit.getScheduler().runTaskLater(Manager.INSTANCE, Runnable {
                            for (i in 0..8) {
                                if (player.inventory.getItem(i)?.type == Material.MUSHROOM_STEW) {
                                    soupsInHotbar++
                                    if (soupsInHotbar == 8) {
                                        simulator.score += 2
                                        simulator.refills += 1
                                        player.closeInventory()
                                        simulator.sendRecraftRefillTime()
                                        simulator.nextTask()
                                    }
                                }
                            }
                        }, 1)
                    }
                }
        }

        listen<PlayerInteractEvent> {
            if (it.action.isRightClick) {
                val player = it.player
                val duelsPlayer = DuelsPlayer.get(player)

                if (player.getHandItem(it.hand)?.hasMark("soupsim") == true) {
                    player.openGUI(SoupsimulatorGUI.guiBuilder(player))
                }
                if (duelsPlayer.isInSoupsimulator()) {
                    val simulator = Soupsimulator.get(player) ?: return@listen
                    if (simulator.state == GameState.INGAME)
                        if (player.getHandItem(it.hand)?.type == Material.MUSHROOM_STEW) {
                            if (simulator.Task == SoupsimulatorTasks.SOUP) {
                                player.inventory.itemInMainHand.type = Material.BOWL

                                async { DataHolder.playerStats[player]?.addEatenSoup() }
                            }
                        }
                }
            }
        }

        listen<PlayerDropItemEvent> {
            if (DuelsPlayer.get(it.player).isInSoupsimulator()) {
                it.isCancelled = it.itemDrop.itemStack.type != Material.BOWL
            }
        }

        listen<InventoryClickEvent> {
            if (it.whoClicked is Player) {
                val player = it.whoClicked as Player
                val duelsPlayer = DuelsPlayer.get(player)
                if (!duelsPlayer.isInSoupsimulator()) {
                    return@listen
                }
                if (it.click == ClickType.SWAP_OFFHAND)
                    it.isCancelled = true
            }
        }

        listen<PlayerSwapHandItemsEvent> {
            if (DuelsPlayer.get(it.player).isInSoupsimulator())
                it.isCancelled = true
        }
    }
}
