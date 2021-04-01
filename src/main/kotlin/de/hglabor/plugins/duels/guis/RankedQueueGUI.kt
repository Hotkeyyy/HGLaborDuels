package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.kits
import de.hglabor.plugins.duels.localization.sendMsg
import de.hglabor.plugins.duels.player.DuelsPlayer
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.runnables.firstAsync
import net.axay.kspigot.runnables.thenSync
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object RankedQueueGUI {

    private val queueItems = hashMapOf<AbstractKit, ItemStack>()

    fun guiBuilder() = kSpigotGUI(GUIType.THREE_BY_NINE) {
        title = "${KColors.DODGERBLUE}Ranked Queue"
        page(1) {
            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            val compound = createRectCompound<AbstractKit>(
                Slots.RowTwoSlotTwo, Slots.RowTwoSlotEight,
                iconGenerator = { queueItems[it] ?: ItemStack(Material.BARRIER) },
                onClick = { clickEvent, element ->
                    queuePlayer(clickEvent.player, element)
                })

            firstAsync { kits.filter { it.allowsRanked } }
                .thenSync { compound.addContent(it) }.execute()
        }
    }

    private fun queuePlayer(player: Player, kit: AbstractKit) {
        val duelsPlayer = DuelsPlayer.get(player)
        if (duelsPlayer.isBusy()) {
            player.sendMsg("command.cantExecuteNow")
            return
        }

        if (duelsPlayer.rankedQueues.contains(kit)) {
            kit.rankedQueue -= player
            duelsPlayer.rankedQueues -= kit
            player.sendMsg("queue.left", mutableMapOf("kit" to kit.name))
            updateQueueItem(kit)
            return
        }

        duelsPlayer.rankedQueues += kit
        kit.rankedQueue += player
        player.sendMsg("queue.join", mutableMapOf("kit" to kit.name))
        updateQueueItem(kit)
        startNewDuelIfEnoughPlayersInQueue(kit)
    }

    private fun queueItem(kit: AbstractKit): ItemStack {
        val item = kit.itemInGUI.clone()
        item.meta {
            addLore {
                +"§8§m                  "
                +"§7In Queue §8» ${KColors.MEDIUMPURPLE}${kit.rankedQueue.size}"
            }
        }
        item.amount = if (kit.rankedQueue.size > 1) kit.rankedQueue.size else 1
        return item
    }

    private fun updateQueueItem(kit: AbstractKit) {
        queueItems[kit] = queueItem(kit)
    }

    private fun startNewDuelIfEnoughPlayersInQueue(kit: AbstractKit) {
        if (kit.rankedQueue.size >= 2) {
            val first = kit.rankedQueue.first()
            val last = kit.rankedQueue.last()
            Duel.create(first, last, kit)
            updateQueueItem(kit)
        }
    }
}