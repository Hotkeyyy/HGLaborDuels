package de.hglabor.plugins.duels.duel.overview

import de.hglabor.plugins.duels.kits.KitType
import de.hglabor.plugins.duels.kits.Kits.Companion.info
import de.hglabor.plugins.duels.utils.Data
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType
import java.io.File
import java.util.*


object DuelOverviewGUI {
    fun openPlayerOverview(whoOpens: Player, gameID: String, playerOneOrTwo: Player) {
        val duel = Data.duelFromID[gameID]
        val file = File("${duel!!.path}//playerdata//${playerOneOrTwo.uniqueId}.yml")
        val yamlConfiguration = YamlConfiguration.loadConfiguration(file)

        val inventory = Bukkit.createInventory(
            null,
            54,
            "${KColors.DODGERBLUE} ${playerOneOrTwo.name}'s Inventory ${KColors.DARKGRAY}|${KColors.GRAY} $gameID"
        )

        val placeholder = itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } }
        for (i in 36..44) {
            inventory.setItem(i, placeholder)
        }
        inventory.setItem(49, placeholder)

        for (slot in 0..8) {
            if (yamlConfiguration["inventory.slot.$slot"] != null)
                inventory.setItem(slot + 27, yamlConfiguration.getItemStack("inventory.slot.$slot.itemStack"))
        }

        for (slot in 9..inventory.size) {
            if (yamlConfiguration["inventory.slot.$slot"] != null)
                inventory.setItem(slot - 9, yamlConfiguration.getItemStack("inventory.slot.$slot.itemStack"))
        }

        if (yamlConfiguration["inventory.slot.helmet"] != null)
            inventory.setItem(45, yamlConfiguration.getItemStack("inventory.slot.helmet.itemStack"))

        if (yamlConfiguration["inventory.slot.chestplate"] != null)
            inventory.setItem(46, yamlConfiguration.getItemStack("inventory.slot.chestplate.itemStack"))

        if (yamlConfiguration["inventory.slot.leggings"] != null)
            inventory.setItem(47, yamlConfiguration.getItemStack("inventory.slot.leggings.itemStack"))

        if (yamlConfiguration["inventory.slot.boots"] != null)
            inventory.setItem(48, yamlConfiguration.getItemStack("inventory.slot.boots.itemStack"))

        val health = itemStack(Material.POPPY) {
            meta {
                name =
                    "${KColors.CORNSILK}Health ${KColors.DARKGRAY}» ${KColors.DODGERBLUE}${yamlConfiguration["data.health"] as Int / 2.0} ❤";
            }
        }
        inventory.setItem(50, health)

        val hits = itemStack(Material.NETHERITE_SWORD) {
            meta {
                name =
                    "${KColors.CORNSILK}Hits ${KColors.DARKGRAY}» ${KColors.DODGERBLUE}${yamlConfiguration["data.hits"]}";
                addLore {
                    +"${KColors.GRAY}Longest Combo ${KColors.DARKGRAY}» ${KColors.MEDIUMPURPLE}${yamlConfiguration["data.longestCombo"]}"
                }
            }
        }
        inventory.setItem(51, hits)

        if (duel.kit.info.type == KitType.SOUP) {
            val soups = itemStack(Material.MUSHROOM_STEW) {
                meta {
                    name =
                        "${KColors.CORNSILK}Soups ${KColors.DARKGRAY}» ${KColors.DODGERBLUE}${yamlConfiguration["data.soupsLeft"]}";
                    addLore {
                        +"${KColors.GRAY}Presouped ${KColors.DARKGRAY}» ${KColors.MEDIUMPURPLE}${yamlConfiguration["data.presoups"]}"
                        +"${KColors.GRAY}Wasted ${KColors.DARKGRAY}» ${KColors.MEDIUMPURPLE}${yamlConfiguration["data.wastedHealth"] as Int / 2.0} ❤"
                    }
                }
            }
            inventory.setItem(52, soups)

        } else if (duel.kit.info.type == KitType.POT) {
            val pot = ItemStack(Material.SPLASH_POTION)
            val potMeta = pot.itemMeta as PotionMeta
            potMeta.basePotionData = PotionData(PotionType.INSTANT_HEAL, false, true)
            potMeta.name = "${KColors.CORNSILK}Potions ${KColors.DARKGRAY}» ${KColors.DODGERBLUE}${yamlConfiguration["data.potsLeft"]}"
            val lore = ArrayList<String>()
            lore.add("${KColors.GRAY}Missed Potions ${KColors.DARKGRAY}» ${KColors.MEDIUMPURPLE}${yamlConfiguration["data.missedPots"]}")
            lore.add("${KColors.GRAY}Wasted ${KColors.DARKGRAY}» ${KColors.MEDIUMPURPLE}${yamlConfiguration["data.wastedHealth"] as Int / 2.0} ❤")
            potMeta.lore = lore
            pot.itemMeta = potMeta
            inventory.setItem(52, pot)
        }

        val otherInventory = itemStack(Material.ARROW) {
            meta {
                name = "${KColors.DODGERBLUE}${duel.getOtherPlayer(playerOneOrTwo).name}'s Inventory"
            }
        }
        inventory.setItem(53, otherInventory)

        whoOpens.openInventory(inventory)
    }

    fun enable() {
        listen<InventoryClickEvent> {
            if (it.view.title.contains("'s Inventory ${KColors.DARKGRAY}|${KColors.GRAY} ")) {
                it.isCancelled = true
                if (it.currentItem != null) {
                    if (it.currentItem!!.type == Material.ARROW) {
                        val gameID = it.view.title.split(" ")[4]
                        val currentPlayer = (it.view.title.split(" ")[1]).split("'")[0]
                        val duel = Data.duelFromID[gameID]
                        openPlayerOverview(
                            it.whoClicked as Player, gameID, duel!!.getOtherPlayer(Bukkit.getPlayer(currentPlayer)!!)
                        )
                    }
                }
            }
        }
    }
}