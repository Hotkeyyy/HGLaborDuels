package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.kits.Kit
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.Kits.Companion.getKit
import de.hglabor.plugins.duels.kits.Kits.Companion.info
import de.hglabor.plugins.duels.kits.kit.Random
import de.hglabor.plugins.duels.kits.kitMap
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.gui.*
import net.axay.kspigot.gui.elements.GUICompoundElement
import net.axay.kspigot.gui.elements.GUIRectSpaceCompound
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.meta
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

object QueueGUI {
    private var menuCompound: GUIRectSpaceCompound<ForInventoryFiveByNine, GUICompoundElement<ForInventoryFiveByNine>>? =
        null

    val gui = kSpigotGUI(GUIType.FIVE_BY_NINE, SharedGUICreator()) {

        title = "${KColors.DODGERBLUE}Queue"

        page(1) {
            placeholder(Slots.RowOneSlotOne rectTo Slots.RowFiveSlotNine, ItemStack(Material.WHITE_STAINED_GLASS_PANE))
            //button(Slots.RowOneSlotFive, queueItem(Kits.RANDOM)) { queuePlayer(it.player, Kits.RANDOM) }

            menuCompound = createSimpleRectCompound(Slots.RowTwoSlotTwo, Slots.RowFourSlotEight)
        }
    }

    fun queuePlayer(player: Player, kit: Kits) {
        if (!player.isInFight()) {
            if (Kits.playerQueue.containsKey(player)) {
                if (Kits.playerQueue[player] == kit) {
                    Kits.playerQueue.remove(player)
                    Kits.queue[kit]?.remove(player)
                    player.sendLocalizedMessage(
                        Localization.QUEUE_LEFT_DE,
                        Localization.QUEUE_LEFT_EN,
                        "%kit%", kit.info.name
                    )
                    updateContents()
                    return
                }
            }
            if (Kits.playerQueue[player] != null) {
                Kits.queue[Kits.playerQueue[player]]?.remove(player)
            }
            Kits.playerQueue[player] = kit
            Kits.queue[kit]?.add(player)
            player.sendLocalizedMessage(
                Localization.QUEUE_JOINED_DE,
                Localization.QUEUE_JOINED_EN,
                "%kit%", kit.info.name
            )
            startNewDuelIfEnoughPlayersInQueue(kit)
            updateContents()
        }
    }

    fun queueItem(kits: Kits): ItemStack {
        val kit = kitMap[kits]
        val newItem = kit!!.itemInGUIs.clone()
        newItem.meta {
            addLore {
                val kits = newItem.getKit()
                +"§8§m                  "
                +"§7In Queue §8» ${KColors.MEDIUMPURPLE}${Kits.queue[kits]?.size}"
                if (kits != Kits.RANDOM)
                    +"§7In Game §8» ${KColors.DODGERBLUE}${Kits.inGame[kits]?.size}"
            }
        }
        return newItem
    }

    fun getContent(itemStack: ItemStack): GUICompoundElement<ForInventoryFiveByNine> {
        return(QueueGUICompoundElement(itemStack))
    }

    fun updateContents() {
        val list = arrayListOf<GUICompoundElement<ForInventoryFiveByNine>>()
        kitMap.keys.forEach {
            list.add(getContent(queueItem(it)))
        }
        menuCompound?.setContent(list)
    }

    class QueueGUICompoundElement(itemStack: ItemStack) :
        GUICompoundElement<ForInventoryFiveByNine>(itemStack, onClick = listen@{
                it.bukkitEvent.isCancelled = true
                val player = it.player
                val kit = it.bukkitEvent.currentItem?.getKit()
                if (kit != null) {
                    queuePlayer(player, kit)
                }
            })

    private fun startNewDuelIfEnoughPlayersInQueue(kit: Kits) {
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