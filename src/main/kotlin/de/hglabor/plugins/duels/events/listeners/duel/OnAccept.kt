package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.duels.utils.Data
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.getHandItem
import net.axay.kspigot.utils.hasMark
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.EquipmentSlot


object OnAccept {
    init {
        listen<EntityDamageByEntityEvent> {
            if (it.damager is Player && it.entity is Player) {
                val damager = it.damager as Player
                val target = it.entity as Player
                val duelsDamager = DuelsPlayer.get(damager)
                val duelsTarget = DuelsPlayer.get(target)

                if (duelsDamager.isBusy() || duelsTarget.isBusy()) return@listen

                it.isCancelled = true
                if (damager.getHandItem(EquipmentSlot.HAND)?.hasMark("duelitem")!!) {
                    if (Data.challenged[target] == damager) {
                        it.isCancelled = true
                        val kit = Data.challengeKit[target] ?: return@listen
                        Duel.create(damager, target, kit)
                    }
                }
            }
        }
    }
}