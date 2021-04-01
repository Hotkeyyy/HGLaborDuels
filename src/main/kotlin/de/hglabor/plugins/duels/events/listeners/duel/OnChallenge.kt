package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.guis.KitsGUI
import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.duels.utils.Data
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.getHandItem
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.utils.hasMark
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.EquipmentSlot

object OnChallenge {
    init {
        listen<PlayerInteractAtEntityEvent> {
            if(it.hand == EquipmentSlot.OFF_HAND) return@listen
            if (it.rightClicked is Player) {
                val player = it.player
                val target = it.rightClicked as Player
                val duelsPlayer = DuelsPlayer.get(player)
                val duelsTarget = DuelsPlayer.get(target)
                if(player.getHandItem(EquipmentSlot.HAND)?.hasMark("duelitem")!!) {
                    if(!(duelsPlayer.isBusy() && duelsTarget.isBusy())) {
                        Data.openedDuelGUI[player] = target
                        Data.openedKitInventory[player] = KitsGUI.KitInventories.DUEL
                        player.openGUI(KitsGUI.guiBuilder())
                    }
                }
            }
        }

        listen<EntityDamageByEntityEvent> {
            if (it.damager is Player && it.entity is Player) {
                val damager = it.damager as Player
                val target = it.entity as Player
                val duelsDamager = DuelsPlayer.get(damager)
                val duelsTarget = DuelsPlayer.get(target)
                if(damager.getHandItem(EquipmentSlot.HAND)?.hasMark("duelitem")!!) {
                    if(!(duelsDamager.isBusy() && duelsTarget.isBusy())) {
                        if (Data.challenged[target] != damager) {
                            it.isCancelled = true
                            Data.openedDuelGUI[damager] = target
                            Data.openedKitInventory[damager] = KitsGUI.KitInventories.DUEL
                            damager.openGUI(KitsGUI.guiBuilder())
                        }
                    }
                } else {
                    return@listen
                }
            }
        }
    }
}