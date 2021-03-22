package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.data.DataHolder
import de.hglabor.plugins.duels.duel.GameState
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.specials.Specials
import de.hglabor.plugins.duels.soupsimulator.Soupsim.isInSoupsimulator
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.event.listen
import net.axay.kspigot.runnables.async
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent


object OnDamage {

    private val NERFED_ITEMS = arrayListOf("_PICKAXE", "_AXE", "_SHOVEL", "TRIDENT")

    init {
        listen<EntityDamageEvent> {
            if (it.entity is EnderCrystal) {
                it.isCancelled = false
            }
            if (it.entity is Player) {
                val player = it.entity as Player
                if (player.isInFight()) {
                    val duel = Data.duelFromPlayer(player)
                    if (duel.state == GameState.RUNNING) {
                        if (duel.kit.specials.contains(Specials.INVINICIBLE)) {
                            it.isCancelled = true
                            return@listen
                        }
                        if (duel.kit.specials.contains(Specials.NODAMAGE))
                            it.damage = 0.0
                    } else {
                        it.isCancelled = true
                    }
                }
                if (player.location.world != Bukkit.getWorld("FightWorld"))
                    if (!player.isInSoupsimulator())
                        it.isCancelled = true
            }
        }

        listen<EntityDamageByEntityEvent>(priority = EventPriority.HIGHEST) {
            if (it.entity is EnderCrystal) {
                it.isCancelled = false
            }
            if (it.entity is Player) {
                val player = it.entity as Player
                if (player.isInFight()) {
                    var damager: Player? = null
                    var damage = it.damage
                    val duel = Data.duelFromPlayer(player)

                    if (it.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                        if (it.damager is Player) {
                            damager = it.damager as Player
                            if (damager.isInFight()) {
                                if (duel.state == GameState.RUNNING) {
                                    damage = getFinalDamage(duel.kit, damage, damager.inventory.itemInMainHand.type)
                                } else {
                                    it.isCancelled = true
                                }
                            } else {
                                it.isCancelled = true
                            }
                        }
                    } else if (it.cause == EntityDamageEvent.DamageCause.PROJECTILE) {
                        if (it.damager is Arrow || it.damager is Trident) {
                            val d = it.damager as AbstractArrow
                            if (d.shooter is Player)
                                damager = d.shooter as Player
                            if (d is Arrow) {
                                damage *= 0.67
                            }
                        }
                    }
                    if (!it.isCancelled) {
                        it.damage = damage
                        if (damager != null) {
                            duel.hits[damager] = duel.hits[damager]!! + 1

                            duel.lastHitOfPlayer[damager] = player
                            duel.lastAttackerOfPlayer[player] = damager

                            duel.currentCombo[damager] = duel.currentCombo[damager]!! + 1
                            duel.currentCombo[player] = 0
                            if (duel.longestCombo[damager]!! < duel.currentCombo[damager]!!) {
                                duel.longestCombo[damager] = duel.currentCombo[damager]!!
                            }

                            async { DataHolder.playerStats[damager]?.addTotalHit() }
                        }
                    }
                }
            }
        }
    }

    private fun getFinalDamage(kit: AbstractKit, damage: Double, material: Material): Double {
        val itemName = material.name
        var finalDamage = damage

        if (kit.specials.contains(Specials.HITCOOLDOWN))
            return finalDamage
        if (kit.specials.contains(Specials.NODAMAGE))
            finalDamage = 0.0
        if (itemName.contains("_SWORD"))
            if (itemName.contains("DIA"))
                finalDamage *= 0.8
            else
                finalDamage *= 0.5
        for (nerfedItem in NERFED_ITEMS)
            if (itemName.endsWith(nerfedItem))
                finalDamage *= 0.2

        return finalDamage
    }
}