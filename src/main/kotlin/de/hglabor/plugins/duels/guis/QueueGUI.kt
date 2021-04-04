package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.KitCategory
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.kits
import de.hglabor.plugins.duels.utils.sendMsg
import de.hglabor.plugins.duels.player.DuelsPlayer
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.gui.*
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.runnables.firstAsync
import net.axay.kspigot.runnables.thenSync
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object QueueGUI {

    private val queueItems = hashMapOf<AbstractKit, ItemStack>()

    fun guiBuilder() = kSpigotGUI(GUIType.SIX_BY_NINE) {
        title = "${KColors.DODGERBLUE}Unranked Queue"
        page(1) {
            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            val categoryGUICompound = createRectCompound<KitCategory>(Slots.RowFiveSlotTwo, Slots.RowFiveSlotEight,
                iconGenerator = { it.itemStack },
                onClick = { clickEvent, element ->
                    placeholder(Slots.RowFourSlotTwo linTo Slots.RowFourSlotEight,
                        itemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE) { meta { name = null } })
                    placeholder(element.magentaSlot,
                        itemStack(Material.MAGENTA_STAINED_GLASS_PANE) { meta { name = null } })

                    val compound = createRectCompound<AbstractKit>(Slots.RowTwoSlotTwo, Slots.RowThreeSlotEight,
                        iconGenerator = { queueItems[it] ?: ItemStack(Material.BARRIER) },
                        onClick = { clickEvent, element ->
                            queuePlayer(clickEvent.player, element)
                        })

                    if (element.itemStack.isSimilar(KitCategory.MAIN.itemStack)) {
                        firstAsync { kits.filter { Kits.mainKits.contains(it) } }
                            .thenSync { compound.addContent(it) }.execute()
                    } else {
                        firstAsync { kits.filter { it.category == element } }
                            .thenSync { compound.addContent(it) }.execute()
                    }
                    clickEvent.guiInstance.gotoPage(1)
                })
            firstAsync { listOf(*KitCategory.values()) }
                .thenSync { categoryGUICompound.addContent(it) }.execute()

            placeholder(Slots.RowFourSlotTwo linTo Slots.RowFourSlotEight,
                itemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE) { meta { name = null } })
            placeholder(Slots.RowFourSlotTwo, itemStack(Material.MAGENTA_STAINED_GLASS_PANE) { meta { name = null } })

            val compound = createRectCompound<AbstractKit>(Slots.RowTwoSlotTwo, Slots.RowThreeSlotEight,
                iconGenerator = { queueItems[it] ?: ItemStack(Material.BARRIER) },
                onClick = { clickEvent, element ->
                    queuePlayer(clickEvent.player, element)
                })
            firstAsync { kits.filter { Kits.mainKits.contains(it) } }
                .thenSync { compound.addContent(it) }.execute()
        }
    }

    private fun queuePlayer(player: Player, kit: AbstractKit) {
        val duelsPlayer = DuelsPlayer.get(player)
        if (duelsPlayer.isBusy()) {
            player.sendMsg("command.cantExecuteNow")
            return
        }

        if (duelsPlayer.unrankedQueues.contains(kit)) {
            kit.unrankedQueue -= player
            duelsPlayer.unrankedQueues -= kit
            player.sendMsg("queue.left", mutableMapOf("kit" to kit.name))
            updateQueueItem(kit)
            return
        }

        duelsPlayer.unrankedQueues += kit
        kit.unrankedQueue += player
        player.sendMsg("queue.join", mutableMapOf("kit" to kit.name))
        updateQueueItem(kit)
        startNewDuelIfEnoughPlayersInQueue(kit)
    }

    private fun queueItem(kit: AbstractKit): ItemStack {
        val item = kit.itemInGUI.clone()
        item.meta {
            addLore {
                +"§8§m                  "
                +"§7In Queue §8» ${KColors.MEDIUMPURPLE}${kit.unrankedQueue.size}"
            }
        }
        item.amount = if (kit.unrankedQueue.size > 1) kit.unrankedQueue.size else 1
        return item
    }

    fun updateQueueItem(kit: AbstractKit) {
        queueItems[kit] = queueItem(kit)
    }

    private fun startNewDuelIfEnoughPlayersInQueue(kit: AbstractKit) {
        if (kit.unrankedQueue.size >= 2) {
            val first = kit.unrankedQueue.first()
            val last = kit.unrankedQueue.last()
            Duel.create(first, last, kit)
            updateQueueItem(kit)
        }
    }
}
