package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.kit.Random
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.party.Party
import de.hglabor.plugins.duels.party.Partys.isInParty
import de.hglabor.plugins.duels.tournament.Tournament
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.duel
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.gui.ForInventoryFiveByNine
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.elements.GUICompoundElement
import net.axay.kspigot.gui.elements.GUIRectSpaceCompound
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

object ChooseKitGUI {
    private var menuCompound: GUIRectSpaceCompound<ForInventoryFiveByNine, GUICompoundElement<ForInventoryFiveByNine>>? =
        null

    val gui = kSpigotGUI(GUIType.FIVE_BY_NINE) {

        title = "${KColors.DODGERBLUE}Choose Kit"

        page(1) {

            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            placeholder(Slots.RowOneSlotFive, Random().itemInGUIs())

            menuCompound = createSimpleRectCompound(Slots.RowTwoSlotTwo, Slots.RowFourSlotEight)

        }
    }

    class KitsGUICompoundElement(itemStack: ItemStack) : GUICompoundElement<ForInventoryFiveByNine>(itemStack)

    fun addContent(element: GUICompoundElement<ForInventoryFiveByNine>) {
        menuCompound?.addContent(element)
    }

    fun enable() {
        listen<InventoryCloseEvent> {
            if (it.view.title == "${KColors.DODGERBLUE}Choose Kit") {
                Data.openedDuelGUI.remove(it.player)
                Data.openedKitInventory.remove(it.player)
            }
        }

        listen<InventoryClickEvent> {
            if (it.view.title == "${KColors.DODGERBLUE}Choose Kit") {
                if (it.whoClicked is Player) {
                    val player = it.whoClicked as Player
                    if (it.currentItem?.type != Material.WHITE_STAINED_GLASS_PANE) {
                        val itemName = it.currentItem?.itemMeta?.displayName
                        val kitName = itemName?.removeRange(0, 14)?.toUpperCase()?.replace(" ", "")
                        val kit: Kits = Kits.valueOf(kitName!!)

                        if (Data.openedKitInventory.containsKey(player)) {
                            if (Data.openedKitInventory[player] == Data.KitInventories.DUEL) {
                                if (kit == Kits.JUMPANDRUN) {
                                    if (player.isInParty() || Data.openedDuelGUI[player]!!.isInParty()) {
                                        player.sendLocalizedMessage(
                                            Localization.CANT_USE_KIT_IN_PARTY_DE,
                                            Localization.CANT_USE_KIT_IN_PARTY_EN
                                        )
                                        return@listen
                                    }
                                }
                                Data.openedDuelGUI[player]?.let { it1 -> player.duel(it1, kit) }

                            } else if (Data.openedKitInventory[player] == Data.KitInventories.SPLITPARTY) {
                                val team = Party.get(player)!!
                                if (kit == Kits.JUMPANDRUN) {
                                    player.sendLocalizedMessage(
                                        Localization.CANT_USE_KIT_IN_PARTY_DE,
                                        Localization.CANT_USE_KIT_IN_PARTY_EN
                                    )
                                    return@listen
                                }
                                Duel.create(team.getSplitTeams().first, team.getSplitTeams().second, kit)
                            } else if (Data.openedKitInventory[player] == Data.KitInventories.TOURNAMENT)
                                Tournament.createPublic(player, kit)
                            player.closeInventory()
                        }
                    }
                }
            }
        }
    }
}

