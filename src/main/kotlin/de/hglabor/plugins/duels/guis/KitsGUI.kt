package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.kits.kit.`fun`.HardJumpAndRun
import de.hglabor.plugins.duels.kits.kit.`fun`.JumpAndRun
import de.hglabor.plugins.duels.utils.Localization
import de.hglabor.plugins.duels.utils.sendMsg
import de.hglabor.plugins.duels.party.Party
import de.hglabor.plugins.duels.party.Partys.hasParty
import de.hglabor.plugins.duels.party.Partys.isInParty
import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.duels.tournament.Tournament
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.challenge
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.gui.*
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.runnables.firstAsync
import net.axay.kspigot.runnables.task
import net.axay.kspigot.runnables.thenSync
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

object KitsGUI {
    enum class KitInventories { DUEL, TOURNAMENT, SPLITPARTY, INVENTORYSORTING }

    fun guiBuilder() = kSpigotGUI(GUIType.SIX_BY_NINE) {
        title = "${KColors.DODGERBLUE}Kits"
        page(1) {
            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            val categoryGUICompound = createRectCompound<KitCategory>(Slots.RowFiveSlotTwo, Slots.RowFiveSlotEight,
                iconGenerator = { it.itemStack },
                onClick = { clickEvent, element ->
                    clickEvent.bukkitEvent.isCancelled = true
                    placeholder(Slots.RowFourSlotTwo linTo Slots.RowFourSlotEight,
                        itemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE) { meta { name = null } })
                    placeholder(element.magentaSlot,
                        itemStack(Material.MAGENTA_STAINED_GLASS_PANE) { meta { name = null } })

                    placeholder(Slots.RowTwoSlotTwo rectTo Slots.RowThreeSlotEight, ItemStack(Material.AIR))
                    val compound = createRectCompound<AbstractKit>(Slots.RowTwoSlotTwo, Slots.RowThreeSlotEight,
                        iconGenerator = { it.itemInGUI },
                        onClick = { clickEvent, element ->
                            clickEvent.bukkitEvent.isCancelled = true
                            chooseKit(clickEvent.player, element)
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
                iconGenerator = { it.itemInGUI },
                onClick = { clickEvent, element ->
                    clickEvent.bukkitEvent.isCancelled = true
                    chooseKit(clickEvent.player, element)
                })
            firstAsync { kits.filter { Kits.mainKits.contains(it) } }
                .thenSync { compound.addContent(it) }.execute()
        }
    }

    private fun chooseKit(player: Player, kit: AbstractKit) {
        if (Data.openedKitInventory.containsKey(player)) {
            when (Data.openedKitInventory[player]) {
                KitInventories.DUEL -> {
                    if (kit == JumpAndRun || kit == HardJumpAndRun) {
                        if (player.isInParty() || Data.openedDuelGUI[player]!!.isInParty()) {
                            player.sendMsg("kits.fail.cantUseInParty")
                            return
                        }
                    }
                    val enemy = Data.openedDuelGUI[player]
                    if (enemy != null) {
                        if (enemy.isInParty() && !enemy.hasParty()) {
                            player.sendMsg("duel.fail.playerInParty", mutableMapOf("playerName" to enemy.name))
                            return
                        }
                        if (!(Data.challenged[player] == enemy && Data.challengeKit[player] == kit)) {
                            player.challenge(enemy, kit)
                        }
                    }
                    Data.openedKitInventory.remove(player)
                    player.closeInventory()
                }

                KitInventories.SPLITPARTY -> {
                    if (kit == JumpAndRun || kit == HardJumpAndRun) {
                        if (player.isInParty() || Data.openedDuelGUI[player]!!.isInParty()) {
                            player.sendMsg("kits.fail.cantUseInParty")
                            return
                        }
                    }
                    val party = Party.get(player)!!
                    Duel.create(party.getSplitTeams().first, party.getSplitTeams().second, kit)
                    Data.openedKitInventory.remove(player)
                    player.closeInventory()
                }

                KitInventories.TOURNAMENT -> {
                    Tournament.createPublic(player, kit)
                    Data.openedKitInventory.remove(player)
                    player.closeInventory()
                }

                KitInventories.INVENTORYSORTING -> {
                    task(true, 2) {
                        player.inventory.clear()
                        val inventory = Bukkit.createInventory(null, 36, kit.name)
                        val kitInventory = DuelsPlayer.get(player).inventorySorting.inventories[kit] ?: kit.defaultInventory
                        kitInventory.forEach { (slot, item) ->
                            if (slot < 9) {
                                inventory.setItem(slot + 27, item)
                            } else {
                                inventory.setItem(slot - 9, item)
                            }
                        }
                        player.inventory.setItem(22, itemStack(Material.DAMAGED_ANVIL) {
                            meta {
                                name = Localization.getMessage("inventorysort.inventory.resetKit.name", player)
                                addLore {
                                    +Localization.getMessage("inventorysort.inventory.resetKit.lore.1", player)
                                    +Localization.getMessage("inventorysort.inventory.resetKit.lore.2", player)
                                }
                            }
                        })
                        player.openInventory(inventory)
                        Data.openedKitInventory[player] = KitInventories.INVENTORYSORTING
                    }
                }

                else -> {
                    player.sendMessage("Error with opened inventory, please try again")
                }
            }
        }
    }

    fun enable() {
        listen<InventoryCloseEvent> {
            val player = it.player
            if (player is Player) {
                if (it.view.title == "${KColors.DODGERBLUE}Kits") {
                    Data.openedDuelGUI.remove(player)
                    Data.openedKitInventory.remove(player)
                }
            }
        }
    }
}