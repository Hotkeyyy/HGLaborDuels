package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.database.data.DataHolder
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.specials.Specials
import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.duels.utils.Data
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
                val duelsPlayer = DuelsPlayer.get(player)
                if (duelsPlayer.isInFight()) {
                    val duel = duelsPlayer.currentDuel() ?: return@listen

                    if (duel.gameState == Data.GameState.INGAME) {
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
                    if (!duelsPlayer.isInSoupsimulator())
                        it.isCancelled = true
            }
        }

        listen<EntityDamageByEntityEvent>(priority = EventPriority.HIGHEST) {
            if (it.entity is EnderCrystal) {
                it.isCancelled = false
            }
            if (it.entity is Player) {
                val player = it.entity as Player
                val duelsPlayer = DuelsPlayer.get(player)
                if (duelsPlayer.isInFight()) {
                    var damager: Player? = null
                    var damage = it.damage
                    val duel = duelsPlayer.currentDuel() ?: return@listen

                    if (it.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                        if (it.damager is Player) {
                            damager = it.damager as Player
                            val duelsDamager = DuelsPlayer.get(damager)
                            if (duelsDamager.isInFight()) {
                                if (duel.gameState == Data.GameState.INGAME) {
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
                            duel.stats[damager]?.set("hits", (duel.stats[damager]?.get("hits")?: 0) as Int + 1)

                            duel.stats[damager]?.set("lastHit", player)
                            duel.stats[damager]?.set("hits", (duel.stats[damager]?.get("hits")?: 0) as Int + 1)
                            var currentCombo = (duel.stats[damager]?.get("currentCombo")?: 0) as Int
                            duel.stats[damager]?.set("currentCombo", ++currentCombo)

                            duel.stats[player]?.set("lastAttacker", damager)
                            duel.stats[player]?.set("currentCombo", 0)

                            if (((duel.stats[damager]?.get("longestCombo")?: 0) as Int) < currentCombo) {
                                duel.stats[damager]?.set("longestCombo", currentCombo)
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