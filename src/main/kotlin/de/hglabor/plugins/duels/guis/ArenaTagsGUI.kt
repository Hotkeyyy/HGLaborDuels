package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.localization.Localization
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

object ArenaTagsGUI {
    private var menuCompound: GUIRectSpaceCompound<ForInventoryFourByNine, GUICompoundElement<ForInventoryFourByNine>>? =
        null

    val gui = kSpigotGUI(GUIType.FOUR_BY_NINE) {

        title = Localization.ARENA_CREATEION_TAGSGUI_NAME

        page(1) {

            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            menuCompound = createSimpleRectCompound(Slots.RowTwoSlotTwo, Slots.RowThreeSlotEight)

        }
    }

    class TagsGUICompoundElement(
        material: Material,
        name: String,
        onClick: ((GUIClickEvent<ForInventoryFourByNine>) -> Unit)? = null
    ) : GUICompoundElement<ForInventoryFourByNine>(

        itemStack(material) {
            meta {
                this.name = "${KColors.DEEPSKYBLUE}$name"
            }
        },
        onClick

    )

    fun addContent(element: GUICompoundElement<ForInventoryFourByNine>) {
        menuCompound?.addContent(element)
    }

    fun enable() {
        listen<InventoryCloseEvent> {
            if (it.view.title == "${KColors.DODGERBLUE}Duels") {
                Data.openedDuelGUI.remove(it.player)
            }
        }
    }

}