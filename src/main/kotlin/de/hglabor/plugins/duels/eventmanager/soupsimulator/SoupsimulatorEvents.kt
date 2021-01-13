package de.hglabor.plugins.duels.eventmanager.soupsimulator

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.data.DataHolder
import de.hglabor.plugins.duels.data.PlayerStats
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.soupsimulator.Soupsimulator
import de.hglabor.plugins.duels.soupsimulator.SoupsimulatorLevel
import de.hglabor.plugins.duels.soupsimulator.SoupsimulatorTasks
import de.hglabor.plugins.duels.soupsimulator.gui.SoupsimulatorGUI
import de.hglabor.plugins.duels.soupsimulator.isInSoupsimulator
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.getHandItem
import net.axay.kspigot.extensions.events.isRightClick
import net.axay.kspigot.runnables.async
import net.axay.kspigot.utils.hasMark
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent

object SoupsimulatorEvents {
    fun enable() {
        listen<PlayerDropItemEvent> {
            if (Soupsimulator.inSoupsimulator.contains(it.player)) {
                it.isCancelled = it.itemDrop.itemStack.type == Material.BOWL
                Data.droppedItemInSoupsimulator.add(it.itemDrop)
            }
        }

        listen<EntitySpawnEvent> {
            if (it.entityType == EntityType.DROPPED_ITEM) {
                if(Data.droppedItemInSoupsimulator.contains(it.entity)) {
                    it.isCancelled = true
                    Data.droppedItemInSoupsimulator.remove(it.entity)
                }
            }
        }

        listen<PlayerItemHeldEvent> {
            if (Soupsimulator.inSoupsimulator.contains(it.player)) {
                val player: Player = it.player
                if (Soupsimulator.task[player] == SoupsimulatorTasks.SOUP) {
                    if (player.inventory.getItem(it.newSlot)?.type == Material.STONE_SWORD) {
                        var playerHasSoups = false
                        for (item in player.inventory.contents) {
                            if (item != null && item.type != Material.AIR && item.type != Material.STONE_SWORD) {
                                playerHasSoups = true
                                break
                            }
                        }
                        if (!playerHasSoups) {
                            Soupsimulator.score[player] = Soupsimulator.score[player]!! + 1
                            Soupsimulator.giveNextTask(player)
                        }
                    } else {
                        if (player.inventory.getItem(it.newSlot)?.type == Material.MUSHROOM_STEW || player.inventory.getItem(
                                it.newSlot
                            )?.type == Material.BOWL
                        )
                            return@listen

                        Soupsimulator.wrongHotkeys[player] = Soupsimulator.wrongHotkeys[player]!! + 1
                        if (Soupsimulator.level[player] == SoupsimulatorLevel.EASY) {
                            player.damage(0.000000001)
                        } else {
                            if (player.health - 4 <= 0) {
                                if (player.localization("de"))
                                    player.sendMessage(Localization.SOUPSIMULATOR_END_DIED_DE)
                                else
                                    player.sendMessage(Localization.SOUPSIMULATOR_END_DIED_EN)
                                Soupsimulator.end(player)
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
            if (player.isInSoupsimulator()) {
                if (Soupsimulator.task[player] == SoupsimulatorTasks.RECRAFT) {
                    if (it.currentItem?.type == Material.MUSHROOM_STEW && it.isShiftClick) {
                        Soupsimulator.score[player] = Soupsimulator.score[player]!! + 3
                        Soupsimulator.recrafts[player] = Soupsimulator.recrafts[player]!! + 1
                        Bukkit.getScheduler().runTaskLater(Manager.INSTANCE, Runnable {
                            Soupsimulator.sendRecraftRefillTime(player)
                            Soupsimulator.giveNextTask(player)
                        }, 1)
                    }
                }
            }
        }

        listen<InventoryClickEvent> {
            val player: Player = it.whoClicked as Player
            if (player.isInSoupsimulator()) {
                if (Soupsimulator.task[player] == SoupsimulatorTasks.REFILL) {
                    var soupsInHotbar = 0
                    if (it.currentItem?.type == Material.MUSHROOM_STEW && it.isShiftClick) {
                        Bukkit.getScheduler().runTaskLater(Manager.INSTANCE, Runnable {
                            for (i in 0..8) {
                                if (player.inventory.getItem(i)?.type == Material.MUSHROOM_STEW) {
                                    soupsInHotbar++
                                    if (soupsInHotbar == 8) {
                                        Soupsimulator.score[player] = Soupsimulator.score[player]!! + 2
                                        Soupsimulator.refills[player] = Soupsimulator.refills[player]!! + 1
                                        Soupsimulator.sendRecraftRefillTime(player)
                                        Soupsimulator.giveNextTask(player)
                                    }
                                }
                            }
                        }, 1)
                    }
                }
            }
        }

        listen<PlayerInteractEvent> {
            if (it.action.isRightClick) {
                val player = it.player
                if (player.getHandItem(it.hand)?.hasMark("soupsim") == true) {
                    SoupsimulatorGUI.open(player)
                }
                if (Soupsimulator.inSoupsimulator.contains(player)) {
                    if (player.getHandItem(it.hand)?.type == Material.MUSHROOM_STEW) {
                        if (Soupsimulator.task[player] == SoupsimulatorTasks.SOUP || Soupsimulator.level[player] != SoupsimulatorLevel.BONUS) {
                            player.inventory.itemInMainHand.type = Material.BOWL

                            async { DataHolder.playerStats[player]?.addEatenSoup() }
                        }
                    }
                }
            }
        }

        listen<PlayerDropItemEvent> {
            if (Soupsimulator.inSoupsimulator.contains(it.player)) {
                it.isCancelled = it.itemDrop.itemStack.type != Material.BOWL
            }
        }
    }
}
