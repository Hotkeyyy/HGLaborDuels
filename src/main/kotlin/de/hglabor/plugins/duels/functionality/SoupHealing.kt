package de.hglabor.plugins.duels.functionality

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.data.DataHolder
import de.hglabor.plugins.duels.player.DuelsPlayer
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.events.isRightClick
import net.axay.kspigot.runnables.async
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapelessRecipe

object SoupHealing {
    init {
        listen<PlayerInteractEvent> {
            if (it.action.isRightClick || it.action == Action.LEFT_CLICK_BLOCK) {
                val player = it.player
                val duelsPlayer = DuelsPlayer.get(player)
                var consumed = false
                var presouped = false
                var wastedHealth = 0

                if (duelsPlayer.isInFight()) {
                    val duel = duelsPlayer.currentDuel() ?: return@listen

                    if (it.hasItem() && it.material == Material.MUSHROOM_STEW) {
                        if (it.hand == EquipmentSlot.OFF_HAND) return@listen
                        if (player.health != 20.0) {
                            if (player.health > 13) {
                                player.health = 20.0
                                presouped = true
                                wastedHealth = (player.health.toInt() + 7 - 20)
                            } else if (player.health <= 13)
                                player.health = player.health + 7
                            consumed = true

                        } else {
                            if (player.foodLevel != 20) {
                                if (player.foodLevel >= 12)
                                    player.health = 20.0
                                else if (player.health < 12)
                                    player.health = player.health + 8
                                consumed = true
                                player.inventory.itemInMainHand.type = Material.BOWL
                            }
                        }

                        if (consumed) {
                            player.inventory.itemInMainHand.type = Material.BOWL
                            async { DataHolder.playerStats[player]?.addEatenSoup() }
                            if (presouped) {
                                duel.presoups[player] = (duel.presoups[player] ?: 0) + 1
                                duel.wastedHealth[player] = (duel.wastedHealth[player] ?: 0) + wastedHealth
                            }
                        }
                    }
                }
            }
        }

        val cocoSoup = ShapelessRecipe(NamespacedKey(Manager.INSTANCE, "cocoSoup"), ItemStack(Material.STICK))
            .addIngredient(Material.BOWL).addIngredient(Material.COCOA_BEANS)
        Manager.INSTANCE.server.addRecipe(cocoSoup);

    }
}