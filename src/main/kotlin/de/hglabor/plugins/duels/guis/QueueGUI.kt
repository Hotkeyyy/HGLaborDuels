package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.kitMap
import de.hglabor.plugins.duels.utils.Data
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.runnables.async
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

object QueueGUI {
    fun open(player: Player) {

        val inventory = Bukkit.createInventory(null, 36, "${KColors.DODGERBLUE}Queue")
        for (i in 0..8)
            inventory.setItem(i, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
        for (i in 27..35)
            inventory.setItem(i, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
        inventory.setItem(9, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
        inventory.setItem(17, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
        inventory.setItem(18, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
        inventory.setItem(26, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

        for (kit in Kits.values()) {
            val itemStack = kitMap[kit]?.itemInGUIs()
            itemStack?.meta {
                addLore {
                    +"§8§m                  "
                    +"§7In Queue §8» ${KColors.MEDIUMPURPLE}${Kits.queue[kit]?.size}"
                    +"§7In Game §8» ${KColors.DODGERBLUE}${Kits.inGame[kit]}"
                }
            }
            inventory.addItem(itemStack)
        }
        Data.openedQueue[player] = inventory
        player.openInventory(inventory)
    }

    fun updateItems() {
        async {
            val blacklistedSlots = arrayListOf(9, 18, 17, 26)
            Data.openedQueue.keys.forEach {
                var i = 10
                for (kit in Kits.values()) {
                    val itemStack = kitMap[kit]?.itemInGUIs()
                    itemStack?.meta {
                        addLore {
                            +"§8§m                  "
                            +"§7In Queue §8» ${KColors.MEDIUMPURPLE}${Kits.queue[kit]?.size}"
                            +"§7In Game §8» ${KColors.DODGERBLUE}${Kits.inGame[kit]}"
                        }
                    }
                    Data.openedQueue[it]?.setItem(i, itemStack)
                    i++
                    while (blacklistedSlots.contains(i))
                        i++
                }
            }
        }
    }

    fun enable() {
        listen<InventoryClickEvent> {
            val player = it.whoClicked as Player

            if (it.view.title == "${KColors.DODGERBLUE}Queue") {
                if (it.currentItem?.type != Material.WHITE_STAINED_GLASS_PANE) {
                    val itemName = it.currentItem?.itemMeta?.displayName
                    val kitName = itemName?.removeRange(0, 14)!!.toUpperCase().replace(" ", "")
                    val kit: Kits
                    try {
                        kit = Kits.valueOf(kitName)
                    } catch (e: IllegalArgumentException) {
                        return@listen
                    }

                    if (Kits.playerQueue.containsKey(player)) {
                        if (Kits.playerQueue[player] == kit) {
                            Kits.playerQueue.remove(player)
                            Kits.queue[kit]?.remove(player)
                            player.sendMessage("queue verlassen")
                            return@listen
                        }
                    }
                    if (Kits.playerQueue[player] != null)
                        Kits.queue[Kits.playerQueue[player]]?.remove(player)
                    Kits.playerQueue[player] = kit
                    Kits.queue[kit]?.add(player)
                    player.sendMessage("queue für §3${kit.name} §rbetreten")
                    updateItems()
                    startNewDuelIfEnoughPlayersInQueue(kit)
                }
            }
        }

        listen<InventoryCloseEvent> {
            if (it.view.title == "${KColors.DODGERBLUE}Queue") {
                Data.openedQueue.remove(it.player)
            }
        }
    }

    private fun startNewDuelIfEnoughPlayersInQueue(kit: Kits) {
        val list = Kits.queue[kit]!!
        if (list.size > 1) {
            Duel.create(list.first(), list.last(), kit)
            list.remove(list.first())
            list.remove(list.last())
            Kits.playerQueue.remove(list.first())
            Kits.playerQueue.remove(list.last())
            Kits.queue[kit]!!.remove(list.first())
            Kits.queue[kit]!!.remove(list.last())

        }
    }
}