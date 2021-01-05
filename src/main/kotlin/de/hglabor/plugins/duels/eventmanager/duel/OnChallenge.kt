package de.hglabor.plugins.duels.eventmanager.duel

import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.getHandItem
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.utils.hasMark
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.EquipmentSlot

object OnChallenge {
    fun enable() {
        listen<PlayerInteractAtEntityEvent> {
            if (it.rightClicked is Player) {
                val player = it.player
                val target = it.rightClicked as Player
                if(player.getHandItem(EquipmentSlot.HAND)?.hasMark("duelitem")!!) {
                    if(!(player.isInFight() && target.isInFight())) {
                        Data.openedDuelGUI[player] = target
                        player.openGUI(ChooseKitGUI.gui)
                    }
                }
            }
        }

        listen<EntityDamageByEntityEvent> {
            if (it.damager is Player && it.entity is Player) {
                val damager = it.damager as Player
                val target = it.entity as Player
                if(damager.getHandItem(EquipmentSlot.HAND)?.hasMark("duelitem")!!) {
                    if(!(damager.isInFight() && target.isInFight())) {
                        if (Data.challenged[target] != damager) {
                            it.isCancelled = true
                            Data.openedDuelGUI[damager] = target
                            damager.openGUI(ChooseKitGUI.gui)
                        }
                    }
                }
            }
        }
    }
}