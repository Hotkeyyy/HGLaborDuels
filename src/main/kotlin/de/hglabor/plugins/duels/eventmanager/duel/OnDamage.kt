package de.hglabor.plugins.duels.eventmanager.duel

import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.Kits.Companion.info
import de.hglabor.plugins.duels.soupsimulator.isInSoupsimulator
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.getStats
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.broadcast
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

object OnDamage {

    fun enable() {
        listen<EntityDamageEvent> {
            if (it.entity is Player) {
                val player = it.entity as Player
                if (player.isInFight()) {
                    val duelID = Data.duelIDFromPlayer[player]
                    val duel = Data.duelFromID[duelID]
                    if (!duel!!.hasEnded) {
                        var damage = it.damage

                        if (!duel.kit.info.specials.contains("1.16")) {
                            if (it.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK)
                                damage *= 0.55
                        }

                        if (duel.kit.info.specials.contains("nodamage"))
                            it.damage = 0.0

                        if (player.health - damage <= 0.00) {
                            it.isCancelled = true
                            duel.winner = duel.getOtherPlayer(player)
                            duel.loser = player
                            duel.stop()
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

        listen<EntityDamageByEntityEvent> {
            if (it.entity is Player && it.damager is Player) {
                val player = it.entity as Player
                if (player.isInFight() && (it.damager as Player).isInFight()) {
                    val damager = it.damager as Player
                    val duel = Data.duelFromPlayer(player)

                    if (duel.kit.info.specials != null) {
                        if (!duel.kit.info.specials!!.contains("1.16")) {
                            it.damage *= 0.55
                        }

                        if (duel.kit.info.specials!!.contains("nodamage"))
                            it.damage = 0.0
                    }

                    if (damager.inventory.itemInMainHand.type == Material.TRIDENT)
                        it.damage = 4.0


                    if (!duel.hasEnded) {
                        val entity = it.entity as Player
                        duel.hits[damager] = duel.hits[it.damager]!! + 1

                        duel.currentCombo[damager] = duel.currentCombo[it.damager]!! + 1
                        duel.currentCombo[entity] = 0
                        if (duel.longestCombo[damager]!! < duel.currentCombo[damager]!!) {
                            duel.longestCombo[damager] = duel.currentCombo[damager]!!
                        }

                        damager.getStats().addTotalHit()
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