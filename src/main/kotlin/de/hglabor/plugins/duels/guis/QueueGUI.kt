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
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

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
        player.openInventory(inventory)
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

                    val playerList = Kits.queue[kit]!!
                    if (Kits.playerQueue.containsKey(player)) {
                        if (Kits.playerQueue[player] == kit) {
                            playerList.remove(player)
                            Kits.playerQueue.remove(player)
                            Kits.queue[kit] = playerList
                            player.sendMessage("queue verlassen")
                            return@listen
                        }
                    }
                    playerList.add(player)
                    Kits.playerQueue[player] = kit
                    Kits.queue[kit] = playerList
                    player.sendMessage("queue für §3${kit.name} §rbetreten")
                    startNewDuelIfEnoughPlayersInQueue(kit)
                }
            }
        }
    }

    fun startNewDuelIfEnoughPlayersInQueue(kit: Kits) {
        val list = Kits.queue[kit]!!
        if (list.size > 1) {
            Duel(list.first(), list.last(), kit, Data.getFreeGameID()).start()
            list.remove(list.first())
            list.remove(list.last())
        }
    }
}