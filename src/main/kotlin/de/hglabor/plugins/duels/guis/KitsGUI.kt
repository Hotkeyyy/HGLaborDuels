package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.KitCategory
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.kit.Random
import de.hglabor.plugins.duels.kits.kit.`fun`.HardJumpAndRun
import de.hglabor.plugins.duels.kits.kit.`fun`.JumpAndRun
import de.hglabor.plugins.duels.kits.kits
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.localization.sendMsg
import de.hglabor.plugins.duels.party.Party
import de.hglabor.plugins.duels.party.Partys.isInParty
import de.hglabor.plugins.duels.tournament.Tournament
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.duel
import net.axay.kspigot.event.listen
import net.axay.kspigot.gui.*
import net.axay.kspigot.gui.elements.GUICompoundElement
import net.axay.kspigot.gui.elements.GUIRectSpaceCompound
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.runnables.firstAsync
import net.axay.kspigot.runnables.thenSync
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent

object  KitsGUI {

    // TODO rework and shorten code

    private var categoryCompound: GUIRectSpaceCompound<ForInventorySixByNine, GUICompoundElement<ForInventorySixByNine>>? = null

    fun guiBuilder(player: Player) = kSpigotGUI(GUIType.SIX_BY_NINE) {

        title = Localization.INSTANCE.getMessage("kitsgui.title", player)

        // Main
        page(1) {

            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            categoryCompound = createSimpleRectCompound(Slots.RowFiveSlotTwo, Slots.RowFiveSlotEight)

            /*val categoryGUICompound = createRectCompound<KitCategory>(Slots.RowFiveSlotTwo, Slots.RowFiveSlotEight,
                iconGenerator = { it.itemStack }, onClick = { clickEvent, element ->
                    clickEvent.guiInstance.gotoPage(element.pageNumber)
                })
            firstAsync { listOf(*KitCategory.values()) }.thenSync { categoryGUICompound.addContent(it) }.execute()*/

            placeholder(Slots.RowFourSlotTwo linTo Slots.RowFourSlotEight,
                itemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE) { meta { name = null } })
            placeholder(Slots.RowFourSlotTwo, itemStack(Material.MAGENTA_STAINED_GLASS_PANE) { meta { name = null } })

            val compound = createRectCompound<AbstractKit>(Slots.RowTwoSlotTwo, Slots.RowThreeSlotEight,
                iconGenerator = { it.itemInGUI }, onClick = { clickEvent, element ->
                    chooseKit(clickEvent.player, element)
                })

            button(Slots.RowOneSlotFive, Random.INSTANCE.itemInGUI) {
                chooseKit(it.player, Random.INSTANCE)
            }

            firstAsync { kits.filter { kit -> Kits.mainKits.contains(kit) } }.thenSync { compound.addContent(it) }.execute()
        }

        // Soup
        page(2) {

            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            val categoryGUICompound = createRectCompound<KitCategory>(Slots.RowFiveSlotTwo, Slots.RowFiveSlotEight,
                iconGenerator = { it.itemStack }, onClick = { clickEvent, element ->
                    clickEvent.guiInstance.gotoPage(element.pageNumber)
                })
            firstAsync { listOf(*KitCategory.values()) }.thenSync { categoryGUICompound.addContent(it) }.execute()

            placeholder(Slots.RowFourSlotTwo linTo Slots.RowFourSlotEight,
                itemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE) { meta { name = null } })
            placeholder(Slots.RowFourSlotThree, itemStack(Material.MAGENTA_STAINED_GLASS_PANE) { meta { name = null } })

            val compound = createRectCompound<AbstractKit>(Slots.RowTwoSlotTwo, Slots.RowThreeSlotEight,
                iconGenerator = { it.itemInGUI }, onClick = { clickEvent, element ->
                    chooseKit(clickEvent.player, element)
                })

            button(Slots.RowOneSlotFive, Random.INSTANCE.itemInGUI) {
                chooseKit(it.player, Random.INSTANCE)
            }

            firstAsync { kits.filter { kit -> kit.category == KitCategory.SOUP } }.thenSync { compound.addContent(it) }.execute()
        }

        // Pot
        page(3) {

            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            val categoryGUICompound = createRectCompound<KitCategory>(Slots.RowFiveSlotTwo, Slots.RowFiveSlotEight,
                iconGenerator = { it.itemStack }, onClick = { clickEvent, element ->
                    clickEvent.guiInstance.gotoPage(element.pageNumber)
                })
            firstAsync { listOf(*KitCategory.values()) }.thenSync { categoryGUICompound.addContent(it) }.execute()

            placeholder(Slots.RowFourSlotTwo linTo Slots.RowFourSlotEight,
                itemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE) { meta { name = null } })
            placeholder(Slots.RowFourSlotFour, itemStack(Material.MAGENTA_STAINED_GLASS_PANE) { meta { name = null } })

            val compound = createRectCompound<AbstractKit>(Slots.RowTwoSlotTwo, Slots.RowThreeSlotEight,
                iconGenerator = { it.itemInGUI }, onClick = { clickEvent, element ->
                    chooseKit(clickEvent.player, element)
                })

            button(Slots.RowOneSlotFive, Random.INSTANCE.itemInGUI) {
                chooseKit(it.player, Random.INSTANCE)
            }

            firstAsync { kits.filter { kit -> kit.category == KitCategory.POT } }.thenSync { compound.addContent(it) }.execute()
        }

        // Uhc
        page(4) {

            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            val categoryGUICompound = createRectCompound<KitCategory>(Slots.RowFiveSlotTwo, Slots.RowFiveSlotEight,
                iconGenerator = { it.itemStack }, onClick = { clickEvent, element ->
                    clickEvent.guiInstance.gotoPage(element.pageNumber)
                })
            firstAsync { listOf(*KitCategory.values()) }.thenSync { categoryGUICompound.addContent(it) }.execute()

            placeholder(Slots.RowFourSlotTwo linTo Slots.RowFourSlotEight,
                itemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE) { meta { name = null } })
            placeholder(Slots.RowFourSlotFive, itemStack(Material.MAGENTA_STAINED_GLASS_PANE) { meta { name = null } })

            val compound = createRectCompound<AbstractKit>(Slots.RowTwoSlotTwo, Slots.RowThreeSlotEight,
                iconGenerator = { it.itemInGUI }, onClick = { clickEvent, element ->
                    chooseKit(clickEvent.player, element)
                })

            button(Slots.RowOneSlotFive, Random.INSTANCE.itemInGUI) {
                chooseKit(it.player, Random.INSTANCE)
            }

            firstAsync { kits.filter { kit -> kit.category == KitCategory.UHC } }.thenSync { compound.addContent(it) }.execute()
        }

        // Cooldown
        page(5) {

            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            val categoryGUICompound = createRectCompound<KitCategory>(Slots.RowFiveSlotTwo, Slots.RowFiveSlotEight,
                iconGenerator = { it.itemStack }, onClick = { clickEvent, element ->
                    clickEvent.guiInstance.gotoPage(element.pageNumber)
                })
            firstAsync { listOf(*KitCategory.values()) }.thenSync { categoryGUICompound.addContent(it) }.execute()

            placeholder(Slots.RowFourSlotTwo linTo Slots.RowFourSlotEight,
                itemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE) { meta { name = null } })
            placeholder(Slots.RowFourSlotSix, itemStack(Material.MAGENTA_STAINED_GLASS_PANE) { meta { name = null } })

            val compound = createRectCompound<AbstractKit>(Slots.RowTwoSlotTwo, Slots.RowThreeSlotEight,
                iconGenerator = { it.itemInGUI }, onClick = { clickEvent, element ->
                    chooseKit(clickEvent.player, element)
                })

            button(Slots.RowOneSlotFive, Random.INSTANCE.itemInGUI) {
                chooseKit(it.player, Random.INSTANCE)
            }
            firstAsync { kits.filter { kit -> kit.category == KitCategory.FUN } }.thenSync { compound.addContent(it) }.execute()

        }

        // Fun
        page(6) {

            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            val categoryGUICompound = createRectCompound<KitCategory>(Slots.RowFiveSlotTwo, Slots.RowFiveSlotEight,
                iconGenerator = { it.itemStack }, onClick = { clickEvent, element ->
                    clickEvent.guiInstance.gotoPage(element.pageNumber)
                })
            firstAsync { listOf(*KitCategory.values()) }.thenSync { categoryGUICompound.addContent(it) }.execute()

            placeholder(Slots.RowFourSlotTwo linTo Slots.RowFourSlotEight,
                itemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE) { meta { name = null } })
            placeholder(Slots.RowFourSlotSeven, itemStack(Material.MAGENTA_STAINED_GLASS_PANE) { meta { name = null } })

            val compound = createRectCompound<AbstractKit>(Slots.RowTwoSlotTwo, Slots.RowThreeSlotEight,
                iconGenerator = { it.itemInGUI }, onClick = { clickEvent, element ->
                    chooseKit(clickEvent.player, element)
                })

            button(Slots.RowOneSlotFive, Random.INSTANCE.itemInGUI) {
                chooseKit(it.player, Random.INSTANCE)
            }
            firstAsync { kits.filter { kit -> kit.category == KitCategory.COOLDOWN } }.thenSync { compound.addContent(it) }.execute()

        }

        // Trash
        page(7) {

            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            val categoryGUICompound = createRectCompound<KitCategory>(Slots.RowFiveSlotTwo, Slots.RowFiveSlotEight,
                iconGenerator = { it.itemStack }, onClick = { clickEvent, element ->
                    clickEvent.guiInstance.gotoPage(element.pageNumber)
                })
            firstAsync { listOf(*KitCategory.values()) }.thenSync { categoryGUICompound.addContent(it) }.execute()

            placeholder(Slots.RowFourSlotTwo linTo Slots.RowFourSlotEight,
                itemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE) { meta { name = null } })
            placeholder(Slots.RowFourSlotEight, itemStack(Material.MAGENTA_STAINED_GLASS_PANE) { meta { name = null } })

            val compound = createRectCompound<AbstractKit>(Slots.RowTwoSlotTwo, Slots.RowThreeSlotEight,
                iconGenerator = { it.itemInGUI }, onClick = { clickEvent, element ->
                    chooseKit(clickEvent.player, element)
                })

            button(Slots.RowOneSlotFive, Random.INSTANCE.itemInGUI) {
                chooseKit(it.player, Random.INSTANCE)
            }

            firstAsync { kits.filter { kit -> kit.category == KitCategory.TRASH } }.thenSync { compound.addContent(it) }.execute()
        }

    }

    private fun fillCategoryCompound() {
        KitCategory.values().forEach { categoryCompound?.addContent(CategoryGUICompoundElement(it)) }
    }

    class CategoryGUICompoundElement(category: KitCategory) : GUICompoundElement<ForInventorySixByNine>(
        category.itemStack,
        onClick = { clickEvent ->
            clickEvent.guiInstance.gotoPage(category.pageNumber)
        }
    )

    private fun chooseKit(player: Player, kit: AbstractKit) {
        if (Data.openedKitInventory.containsKey(player)) {
            if (Data.openedKitInventory[player] == Data.KitInventories.DUEL) {
                if (kit == JumpAndRun.INSTANCE || kit == HardJumpAndRun.INSTANCE) {
                    if (player.isInParty() || Data.openedDuelGUI[player]!!.isInParty()) {
                        player.sendMsg("kits.fail.cantUseInParty")
                        return
                    }
                }
                Data.openedDuelGUI[player]?.let { it1 -> player.duel(it1, kit) }

            } else if (Data.openedKitInventory[player] == Data.KitInventories.SPLITPARTY) {
                val team = Party.get(player)!!
                if (kit == JumpAndRun.INSTANCE || kit == HardJumpAndRun.INSTANCE) {
                    player.sendMsg("kits.fail.cantUseInParty")
                    return
                }
                Duel.create(team.getSplitTeams().first, team.getSplitTeams().second, kit)
            } else if (Data.openedKitInventory[player] == Data.KitInventories.TOURNAMENT)
                Tournament.createPublic(player, kit)
            player.closeInventory()
        }
    }

    fun enable() {
        fillCategoryCompound()
        listen<InventoryCloseEvent> {
            if (it.view.title == Localization.INSTANCE.getMessage("kitsgui.title", it.player as Player)) {
                Data.openedDuelGUI.remove(it.player)
                Data.openedKitInventory.remove(it.player)
            }
        }
    }
}