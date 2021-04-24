package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.bots.*
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.gui.*
import net.axay.kspigot.gui.elements.GUIRectSpaceCompound
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

object BotGUI  {

    fun buildDifficultyGUI(player: Player): GUI<ForInventoryThreeByNine> {

        // zum generieren für das compund, ein icon hat als default einfach AIR als placeholder sonst halt die sachen und ein onClick() am ende
        fun icons(): List<Icon> = listOf(
            Icon(Material.MUSHROOM_STEW, "${KColors.INDIANRED}Health: ${player.botSettings.health.toDouble() / 2} Herzen" ) {
                val botSettings = (it.whoClicked as Player).botSettings
                val addedHealth = if (it.isShiftClick) 10 else 1
                if (it.isLeftClick) botSettings.health -= addedHealth
                else botSettings.health += addedHealth
            },
            Icon(Material.FEATHER, "${KColors.AQUA}Speed: ${player.botSettings.movementSpeed}") {
                val botSettings = (it.whoClicked as Player).botSettings
                if (it.isLeftClick) botSettings.movementSpeed -= 0.01
                else botSettings.movementSpeed += 0.01
            },
            Icon(Material.ARROW, "${KColors.LIGHTGRAY}Range: ${player.botSettings.range}") {
                val botSettings = (it.whoClicked as Player).botSettings
                if (it.isLeftClick) botSettings.range -= 0.1
                else botSettings.range += 0.1
            },
            Icon(),
            Icon(),
            Icon(Material.RED_DYE, "${KColors.DARKRED}RESET") {
                val botSettings = (it.whoClicked as Player).botSettings
                botSettings.reset()
            },
            Icon(Material.LIME_DYE, "${KColors.LIME}START") {
                // hier bot starten mit arena und so
                player.closeInventory()
            }
        )

        return kSpigotGUI(GUIType.THREE_BY_NINE) {
            title = "Difficulty" //TODO sprachen localization oder so
            page(0) {

                transitionFrom = PageChangeEffect.SWIPE_HORIZONTALLY
                transitionTo = PageChangeEffect.SWIPE_HORIZONTALLY

                val botSettings = player.botSettings
                placeholder(Slots.All, ItemStack(Material.GRAY_STAINED_GLASS_PANE))

                button(Slots.RowTwoSlotTwo, itemStack(Material.LIME_CONCRETE) {
                    meta { name = "${KColors.LIME} Easy" }
                }) {
                    botSettings.setDifficulty(BotDifficulty.EASY)
                    // hier dann auch starten vllt
                }

                button(Slots.RowTwoSlotFour, itemStack(Material.YELLOW_CONCRETE) {
                    meta { name = "${KColors.YELLOW}Medium" }
                }) {
                    botSettings.setDifficulty(BotDifficulty.MEDIUM)
                    // hier dann auch starten vllt
                }

                button(Slots.RowTwoSlotSix, itemStack(Material.RED_CONCRETE) {
                    meta { name = "${KColors.RED}Hard" }
                }) {
                    botSettings.setDifficulty(BotDifficulty.HARD)
                    // hier dann auch starten vllt
                }

                button(Slots.RowTwoSlotEight, itemStack(Material.PURPLE_CONCRETE) {
                    meta { name = "${KColors.PURPLE}Cheater" }
                }) {
                    botSettings.setDifficulty(BotDifficulty.CHEATER)
                    // hier dann auch starten vllt
                }

                nextPage(Slots.RowOneSlotNine, itemStack(Material.REPEATER) {
                    meta { name = "${KColors.WHITESMOKE}Custom Settings" }
                })
            }

            page(1) {

                transitionFrom = PageChangeEffect.SWIPE_HORIZONTALLY
                transitionTo = PageChangeEffect.SWIPE_HORIZONTALLY

                placeholder(Slots.All, ItemStack(Material.GRAY_STAINED_GLASS_PANE))
                previousPage(Slots.RowOneSlotOne, itemStack(Material.PAPER) {
                    meta { name = "${KColors.WHITESMOKE}back" }
                })

                lateinit var compound: GUIRectSpaceCompound<*, Icon>
                compound = createRectCompound(
                    Slots.RowTwoSlotTwo,
                    Slots.RowTwoSlotEight,
                    iconGenerator = {
                        itemStack(it.material) {
                            meta { name = it.iconName }
                        }
                    },
                    onClick = { e, icon ->
                        icon.onClick(e.bukkitEvent)
                        e.bukkitEvent.isCancelled = true
                        compound.setContent(icons()) // muss immer neu generiert werden sonmst changed text nid @CODER BLUIEFIROLYl
                    }
                )
                compound.addContent(icons())
            }
        }
    }
}


class Icon(
    val material: Material = Material.AIR,
    val iconName: String = "",
    val onClick: (event: InventoryClickEvent) -> Unit = {}
)