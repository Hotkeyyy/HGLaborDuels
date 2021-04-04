package de.hglabor.plugins.duels.database.data

import InventorySerialization
import com.mongodb.client.model.Filters
import de.hglabor.plugins.duels.database.MongoManager
import de.hglabor.plugins.duels.functionality.MainInventory
import de.hglabor.plugins.duels.guis.KitsGUI
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.kits
import de.hglabor.plugins.duels.kits.nameToKit
import de.hglabor.plugins.duels.utils.sendMsg
import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.duels.utils.Data
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import org.bson.Document
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack
import org.litote.kmongo.MongoOperator
import java.util.*

class InventorySorting(val player: Player) {

    companion object {
        val reset = mutableListOf<Player>()
        fun get(player: Player): InventorySorting {
            if (DataHolder.inventorySorting.containsKey(player)) {
                return DataHolder.inventorySorting[player]!!
            }
            val inventorySorting = InventorySorting(player)
            inventorySorting.load()
            DataHolder.inventorySorting[player] = inventorySorting
            return inventorySorting
        }

        fun enable() {
            listen<InventoryCloseEvent> {
                val player = it.player
                if (player is Player) {
                    if (Data.openedKitInventory[player] == KitsGUI.KitInventories.INVENTORYSORTING) {
                        if (nameToKit.containsKey(it.view.title)) {
                            if (!reset.contains(player)) {
                                val duelsPlayer = DuelsPlayer.get(player)
                                duelsPlayer.inventorySorting.editKit(it.view)
                            }
                            Data.openedKitInventory.remove(it.player)
                            MainInventory.giveItems(player)

                        }
                    }
                }
            }

            listen<InventoryClickEvent> {
                val player = it.whoClicked
                if (player is Player) {
                    if (Data.openedKitInventory[player] == KitsGUI.KitInventories.INVENTORYSORTING) {
                        if (nameToKit.containsKey(it.view.title)) {
                            it.isCancelled = false
                        }
                        if (it.currentItem?.type == Material.DAMAGED_ANVIL) {
                            if (it.view.title != "${KColors.DODGERBLUE}Kits") {
                                it.isCancelled = true
                                reset.add(player)
                                player.closeInventory()
                                val duelsPlayer = DuelsPlayer.get(player)
                                val kit = nameToKit[it.view.title] ?: return@listen
                                duelsPlayer.inventorySorting.inventories[kit] = kit.defaultInventory
                                player.sendMsg("inventorysort.reset", mutableMapOf("kit" to kit.name))
                                reset.remove(player)
                            }
                        }
                    }
                }
            }
        }
    }

    private var values = mutableMapOf<String, Any>()
    var inventories = mutableMapOf<AbstractKit, MutableMap<Int, ItemStack>>()

    fun load() {
        val document =
            MongoManager.inventorySorting.find(Filters.eq("uuid", player.uniqueId.toString())).first()

        if (document == null) {
            MongoManager.inventorySorting.insertOne(toDocument())
            return
        }

        values.putAll(document)
        convertValuesToInventories()
    }

    private fun toDocument(): Document {
        convertInventoriesToValues()
        val document = Document("uuid", player.uniqueId.toString())
        values.forEach(document::append)
        return document
    }

    private fun convertValuesToInventories() {
        // iterate through kits and set kitname tp null if kit not in inventories
        values.forEach { (kitString, serializedInventory) ->
            val kit = nameToKit[kitString] ?: return@forEach
            val deserializedInventory =
                InventorySerialization.deserializeInventory(serializedInventory.toString())
            inventories[kit] = deserializedInventory
        }
    }

    private fun convertInventoriesToValues() {
        for (kit in kits) {
            val inventoryMap = inventories[kit] ?: continue
            val serializedInventory = InventorySerialization.serializeInventory(inventoryMap)
            values[kit.name] = serializedInventory
        }
    }

    fun editKit(inventoryView: InventoryView) {
        val inventoryName = inventoryView.title
        val kit = nameToKit[inventoryName] ?: return
        val inventory = inventoryView.topInventory
        val inventoryMap = mutableMapOf<Int, ItemStack>()

        for (i in 0 until inventory.size) {
            val slot = if (i >= 27) i - 27
            else i + 9
            val item = inventory.getItem(i) ?: continue
            inventoryMap[slot] = item
        }
        if (inventoryMap == kit.defaultInventory) {
            player.sendMsg("inventorysort.notSaving")
        } else {
            player.sendMsg("inventorysort.saved", mutableMapOf("kit" to kit.name))
            inventories[kit] = inventoryMap
        }
    }

    fun update() {
        MongoManager.inventorySorting.updateOne(
            Filters.eq("uuid", player.uniqueId.toString()),
            Document("${MongoOperator.set}", toDocument())
        )
        for (kit in kits) {
            if (values.containsKey(kit.name)) {
                continue
            }
        }
    }
}