package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.utils.Data
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.gui.*
import net.axay.kspigot.gui.elements.GUICompoundElement
import net.axay.kspigot.gui.elements.GUIRectSpaceCompound
import net.axay.kspigot.items.*
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemFlag

object QueueGUI {
    private var menuCompound: GUIRectSpaceCompound<ForInventoryFourByNine, GUICompoundElement<ForInventoryFourByNine>>? =
        null

    val gui = kSpigotGUI(GUIType.FOUR_BY_NINE) {

        title = "${KColors.DODGERBLUE}Duels"

        page(1) {

            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            menuCompound = createSimpleRectCompound(Slots.RowTwoSlotTwo, Slots.RowThreeSlotEight)


        }
    }

    class QueueGUICompoundElement(
        material: Material,
        name: String,
        description: String?,
        onClick: ((GUIClickEvent<ForInventoryFourByNine>) -> Unit)? = null
    ) : GUICompoundElement<ForInventoryFourByNine>(

        itemStack(material) {
            meta {
                this.name = "${KColors.DEEPSKYBLUE}$name"
                if (description != null)
                    lore = description.toLoreList(KColors.MEDIUMPURPLE)
                flag(ItemFlag.HIDE_ATTRIBUTES)
                flag(ItemFlag.HIDE_POTION_EFFECTS)
            }
        },

        onClick
    )

    fun addContent(element: GUICompoundElement<ForInventoryFourByNine>) {
        menuCompound?.addContent(element)
    }

    fun enable() {
        listen<InventoryCloseEvent> {
            if(it.view.title == "${KColors.DODGERBLUE}Duels"){
                Data.openedDuelGUI.remove(it.player)
            }
        }
    }
}