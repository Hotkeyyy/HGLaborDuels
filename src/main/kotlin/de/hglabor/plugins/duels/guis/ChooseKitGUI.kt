package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.Kits.Companion.getKit
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
import net.axay.kspigot.gui.*
import net.axay.kspigot.gui.elements.GUICompoundElement
import net.axay.kspigot.gui.elements.GUIRectSpaceCompound
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
            placeholder(Slots.RowOneSlotOne rectTo Slots.RowFiveSlotNine, ItemStack(Material.WHITE_STAINED_GLASS_PANE))
            //button(Slots.RowOneSlotFive, Random().itemInGUIs) { chooseKit(it.player, Kits.RANDOM) }

            menuCompound = createSimpleRectCompound(Slots.RowTwoSlotTwo, Slots.RowFourSlotEight)
        }
    }

    fun chooseKit(player: Player, kit: Kits) {
        if (Data.openedKitInventory.containsKey(player)) {
            if (Data.openedKitInventory[player] == Data.KitInventories.DUEL) {
                if (kit == Kits.JUMPANDRUN) {
                    if (player.isInParty() || Data.openedDuelGUI[player]!!.isInParty()) {
                        player.sendLocalizedMessage(
                            Localization.CANT_USE_KIT_IN_PARTY_DE,
                            Localization.CANT_USE_KIT_IN_PARTY_EN)
                        return
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
                    return
                }
                Duel.create(team.getSplitTeams().first, team.getSplitTeams().second, kit)
            } else if (Data.openedKitInventory[player] == Data.KitInventories.TOURNAMENT)
                Tournament.createPublic(player, kit)
            player.closeInventory()
        }
    }

    class KitsGUICompoundElement(itemStack: ItemStack) :
        GUICompoundElement<ForInventoryFiveByNine>(itemStack, onClick = listen@{
            it.bukkitEvent.isCancelled = true
            val player = it.player
            val kit = it.bukkitEvent.currentItem?.getKit()

            if (kit != null) {
                chooseKit(player, kit)
            }
        })


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
    }
}

