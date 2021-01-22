package de.hglabor.plugins.duels.eventmanager.duel

import de.hglabor.plugins.duels.data.DataHolder
import de.hglabor.plugins.duels.duel.GameState
import de.hglabor.plugins.duels.kits.Kits.Companion.info
import de.hglabor.plugins.duels.kits.Specials
import de.hglabor.plugins.duels.soupsimulator.Soupsim.isInSoupsimulator
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.event.listen
import net.axay.kspigot.runnables.async
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

object OnDamage {

    fun enable() {
        listen<EntityDamageEvent> {
            if (it.entity is Player) {
                val player = it.entity as Player
                if (player.isInFight()) {
                    val duel = Data.duelFromPlayer(player)
                    if (duel.state == GameState.RUNNING) {
                        var damage = it.damage

                        if (duel.kit.info.specials.contains(Specials.NODAMAGE))
                            it.damage = 0.0

                        if (player.health - damage <= 0.00) {
                            it.isCancelled = true
                            duel.playerDied(player)
                        }
                    } else {
                        it.isCancelled = true
                    }
                }
                if (player.location.world != Bukkit.getWorld("FightWorld"))
                    if (!player.isInSoupsimulator())
                        it.isCancelled = true
            }
        }

        listen<EntityDamageByEntityEvent> (priority = EventPriority.HIGHEST) {
            if (it.entity is Player && it.damager is Player) {
                val player = it.entity as Player
                if (player.isInFight() && (it.damager as Player).isInFight()) {
                    val damager = it.damager as Player
                    val duel = Data.duelFromPlayer(player)
                    var damage = it.damage

                    if (duel.state == GameState.STARTING)
                        it.isCancelled = true

                    if (duel.state == GameState.RUNNING) {
                        if (!duel.kit.info.specials.contains(Specials.HITCOOLDOWN))
                            damage *= 0.55
                        if (duel.kit.info.specials.contains(Specials.NODAMAGE))
                            damage = 0.0
                        if (damager.inventory.itemInMainHand.type == Material.TRIDENT)
                            damage *= 0.33

                        if (!it.isCancelled) {
                            it.damage = damage
                            duel.hits[damager] = duel.hits[it.damager]!! + 1

                            duel.lastHitOfPlayer[damager] = player
                            duel.lastAttackerOfPlayer[player] = damager

                            duel.currentCombo[damager] = duel.currentCombo[it.damager]!! + 1
                            duel.currentCombo[player] = 0
                            if (duel.longestCombo[damager]!! < duel.currentCombo[damager]!!) {
                                duel.longestCombo[damager] = duel.currentCombo[damager]!!
                            }

                            if (player.health - damage <= 0.00) {
                                it.isCancelled = true
                                duel.playerDied(player)
                            }
                            async { DataHolder.playerStats[player]?.addTotalHit() }
                        }
                    } else {
                        it.isCancelled = true
                    }
                } else {
                    it.isCancelled = true
                }
            }
        }
    }
}