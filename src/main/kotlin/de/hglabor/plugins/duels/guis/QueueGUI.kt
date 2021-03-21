package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.KitCategory
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.kit.Random
import de.hglabor.plugins.duels.kits.kits
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.localization.sendMsg
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.gui.*
import net.axay.kspigot.gui.elements.GUICompoundElement
import net.axay.kspigot.gui.elements.GUIRectSpaceCompound
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.runnables.firstAsync
import net.axay.kspigot.runnables.thenSync
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

object QueueGUI {

    // rework with all kits also create ranked queue with "main" kits

    val gui = kSpigotGUI(GUIType.FOUR_BY_NINE) {

        title = "${KColors.DODGERBLUE}Unranked Queue"

        page(1) {

            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            val compound = createRectCompound<AbstractKit>(
                Slots.RowTwoSlotTwo, Slots.RowThreeSlotEight,
                iconGenerator = { queueItem(it) }, onClick = { clickEvent, element ->
                    queuePlayer(clickEvent.player, element)
                })

            button(Slots.RowOneSlotFive, queueItem(Random.INSTANCE)) {
                queuePlayer(it.player, Random.INSTANCE)
            }

            firstAsync { kits.filter { kit -> Kits.mainKits.contains(kit) } }.thenSync { compound.addContent(it) }.execute()
        }
    }

    private fun queuePlayer(player: Player, kit: AbstractKit) {
        if (!player.isInFight()) {
            if (Kits.playerQueue.containsKey(player)) {
                if (Kits.playerQueue[player]?.contains(kit) == true) {
                    Kits.playerQueue.remove(player)
                    Kits.queue[kit]?.remove(player)
                    player.sendMsg("queue.left", mutableMapOf("kit" to kit.name))
                    return
                }
            }

            Kits.playerQueue[player]?.plusAssign(kit)
            Kits.queue[kit]?.add(player)
            player.sendMsg("queue.join", mutableMapOf("kit" to kit.name))
            startNewDuelIfEnoughPlayersInQueue(kit)
        }
    }

    private fun queueItem(kit: AbstractKit): ItemStack {
        val newItem = kit.itemInGUI.clone()
        newItem.meta {
            addLore {
                +"§8§m                  "
                +"§7In Queue §8» ${KColors.MEDIUMPURPLE}${Kits.queue[kit]?.size}"
                if (kit != Random.INSTANCE)
                    +"§7In Game §8» ${KColors.DODGERBLUE}${Kits.inGame[kit]?.size}"
            }
        }
        return newItem
    }

    private fun startNewDuelIfEnoughPlayersInQueue(kit: AbstractKit) {
        if (Kits.queue[kit]?.size!! >= 2) {
            Kits.queue[kit]?.first()?.let {
                Kits.queue[kit]?.last()?.let { it1 ->
                    Duel.create(it, it1, kit)
                }
            }
        }
    }

    fun enable() {
        listen<InventoryCloseEvent> {
            if (it.view.title == "${KColors.DODGERBLUE}Queue") {
                Data.openedDuelGUI.remove(it.player)
                Data.openedKitInventory.remove(it.player)
            }
        }
    }
}
