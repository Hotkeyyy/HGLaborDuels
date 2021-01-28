package de.hglabor.plugins.duels.duel.overview

import de.hglabor.plugins.duels.utils.Data
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

object DuelTeamOverviewGUI {
    fun open(player: Player, duelID: String, team: ArrayList<Player>) {
        val duel = Data.duelFromID[duelID]!!
        val teamText = if (team == duel.teamOne) "${KColors.DEEPSKYBLUE}Team 1" else "${KColors.DEEPPINK}Team 2"

        val inventory = Bukkit.createInventory(null, 27, "${KColors.DODGERBLUE}Duel §8| §r$teamText §8| §7$duelID")

        val placeholder = itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } }

        for (i in 0..9)
            inventory.setItem(i, placeholder)

        for (i in 17..26)
            inventory.setItem(i, placeholder)

        team.forEach {
            val playerHead = ItemStack(Material.PLAYER_HEAD)
            val playerHeadMeta = playerHead.itemMeta as SkullMeta
            playerHeadMeta.owningPlayer = it
            if (duel.alivePlayers.contains(it))
                playerHeadMeta.setDisplayName("${KColors.GREEN}${it.name}")
            else
                playerHeadMeta.setDisplayName("${KColors.RED}${it.name}")
            playerHead.itemMeta = playerHeadMeta

            playerHead.meta {
                addLore { +"total hits ${duel.hits[it]}" }
                addLore { +"longest combo ${duel.longestCombo[it]}" }
            }
            inventory.addItem(playerHead)
        }

        for (i in 10..16)
            if (inventory.getItem(i) == null)
                inventory.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE) { meta { name = null } })

        player.openInventory(inventory)
    }

    fun enable() {
        listen<InventoryClickEvent> {
            val player = it.whoClicked as Player
            if (it.view.title.contains("${KColors.DODGERBLUE}Duel §8| §r")) {
                if (it.currentItem?.type == Material.PLAYER_HEAD) {
                    val item = it.currentItem!!.itemMeta as SkullMeta
                    val owner = item.owningPlayer as Player
                    val gameID = it.view.title.split(" §8| §7")[1]
                    DuelPlayerDataOverviewGUI.open(player, gameID, owner)
                }
            }
        }
    }
}