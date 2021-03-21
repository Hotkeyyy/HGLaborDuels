package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.getHandItem
import net.axay.kspigot.utils.hasMark
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.EquipmentSlot


object OnAccept {
    fun enable() {
        listen<EntityDamageByEntityEvent> {
            if (it.damager is Player && it.entity is Player) {
                val damager = it.damager as Player
                val target = it.entity as Player
                if (!damager.isInFight() && !target.isInFight()) {
                    it.isCancelled = true
                    if (damager.getHandItem(EquipmentSlot.HAND)?.hasMark("duelitem")!!) {
                        if (Data.challenged[target] == damager) {
                            it.isCancelled = true
                            Duel.create(damager, target, Data.challengeKit[target]!!)
                        }
                    }
                }
            }
        }
    }
}