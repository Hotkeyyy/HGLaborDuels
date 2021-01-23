package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.utils.Data
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.gui.*
import net.axay.kspigot.gui.elements.GUICompoundElement
import net.axay.kspigot.gui.elements.GUIRectSpaceCompound
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

object ChooseKitGUI {
    private var menuCompound: GUIRectSpaceCompound<ForInventoryFourByNine, GUICompoundElement<ForInventoryFourByNine>>? =
        null

    val gui = kSpigotGUI(GUIType.FOUR_BY_NINE) {

        title = "${KColors.DODGERBLUE}Choose Kit"

        page(1) {

            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            menuCompound = createSimpleRectCompound(Slots.RowTwoSlotTwo, Slots.RowThreeSlotEight)

        }
    }

    class KitsGUICompoundElement(
        itemStack: ItemStack,
        onClick: ((GUIClickEvent<ForInventoryFourByNine>) -> Unit)? = null
    ) : GUICompoundElement<ForInventoryFourByNine>(
        itemStack,
        onClick
    )

    fun addContent(element: GUICompoundElement<ForInventoryFourByNine>) {
        menuCompound?.addContent(element)
    }

    fun enable() {
        listen<InventoryCloseEvent> {
            if(it.view.title == "${KColors.DODGERBLUE}Choose Kit"){
                Data.openedDuelGUI.remove(it.player)
                Data.openedKitInventory.remove(it.player)
            }
        }
    }

}

