package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.Kits.Companion.info
import de.hglabor.plugins.duels.kits.Kits.Companion.random
import de.hglabor.plugins.duels.kits.kitMap
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
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
import org.bukkit.inventory.ItemStack

object QueueGUI {
    fun open(player: Player) {

        val inventory = Bukkit.createInventory(null, 45, "${KColors.DODGERBLUE}Queue")
        for (i in 0..8)
            inventory.setItem(i, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
        for (i in 36..44)
            inventory.setItem(i, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
        inventory.setItem(9, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
        inventory.setItem(17, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
        inventory.setItem(18, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
        inventory.setItem(26, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
        inventory.setItem(27, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
        inventory.setItem(35, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

        for (kit in Kits.values()) {
            val itemStack = getQueueItem(kit)
            if (kit == Kits.RANDOM)
                inventory.setItem(40, itemStack)
            else
                inventory.addItem(itemStack)
        }

        Data.openedQueue[player] = inventory
        player.openInventory(inventory)
    }

    fun updateItems() {
        async {
            val blacklistedSlots = arrayListOf(9, 18, 17, 26, 27, 35)
            Data.openedQueue.keys.forEach {
                var i = 10
                val kits = Kits.values().toMutableList()
                kits.remove(Kits.RANDOM)
                for (kit in kits) {
                    Data.openedQueue[it]?.setItem(i, getQueueItem(kit))

                    do { i++ } while (blacklistedSlots.contains(i))
                }
                Data.openedQueue[it]?.setItem(40, getQueueItem(Kits.RANDOM))
            }
        }
    }

    fun getQueueItem(kit: Kits) : ItemStack {
        val itemStack = kitMap[kit]?.itemInGUIs()!!
        itemStack.meta {
            addLore {
                +"§8§m                  "
                +"§7In Queue §8» ${KColors.MEDIUMPURPLE}${Kits.queue[kit]?.size}"
                if (kit != Kits.RANDOM)
                +"§7In Game §8» ${KColors.DODGERBLUE}${Kits.inGame[kit]?.size}"
            }
        }
        return itemStack
    }

    fun enable() {
        listen<InventoryClickEvent> {
            val player = it.whoClicked as Player

            if (it.view.title == "${KColors.DODGERBLUE}Queue") {
                it.isCancelled = true
                if (it.currentItem?.type != Material.WHITE_STAINED_GLASS_PANE) {
                    val itemName = it.currentItem?.itemMeta?.displayName
                    val kitName = itemName?.removeRange(0, 14)?.toUpperCase()?.replace(" ", "")
                    val kit: Kits
                    try {
                        kit = Kits.valueOf(kitName!!)
                    } catch (e: IllegalArgumentException) {
                        return@listen
                    }
                    updateItems()
                    if (Kits.playerQueue.containsKey(player)) {
                        if (Kits.playerQueue[player] == kit) {
                            Kits.playerQueue.remove(player)
                            Kits.queue[kit]?.remove(player)
                            player.sendLocalizedMessage(Localization.QUEUE_LEFT_DE,Localization.QUEUE_LEFT_EN, "%kit%", kit.info.name)
                            return@listen
                        }
                    }
                    if (Kits.playerQueue[player] != null)
                        Kits.queue[Kits.playerQueue[player]]?.remove(player)
                    Kits.playerQueue[player] = kit
                    Kits.queue[kit]?.add(player)
                    player.sendLocalizedMessage(Localization.QUEUE_JOINED_DE,Localization.QUEUE_JOINED_EN, "%kit%", kit.info.name)
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
        var finalKit = kit
        if (finalKit == Kits.RANDOM)
            finalKit = random()

        if (Kits.queue[kit]!!.size >= 2)
            Kits.queue[kit]?.first()?.let { Kits.queue[kit]?.last()?.let { it1 ->
                Duel.create(it, it1, finalKit) }
            }
    }
}