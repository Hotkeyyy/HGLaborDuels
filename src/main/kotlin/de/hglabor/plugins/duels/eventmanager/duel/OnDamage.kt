package de.hglabor.plugins.duels.eventmanager.duel

import de.hglabor.plugins.duels.data.DataHolder
import de.hglabor.plugins.duels.data.PlayerStats
import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.duel.GameState
import de.hglabor.plugins.duels.kits.Kit
import de.hglabor.plugins.duels.kits.Kits.Companion.info
import de.hglabor.plugins.duels.kits.kitMap
import de.hglabor.plugins.duels.kits.specials.Specials
import de.hglabor.plugins.duels.soupsimulator.Soupsim.isInSoupsimulator
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.runnables.async
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.entity.Trident
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent


object OnDamage {

    val NERFED_ITEMS = arrayListOf("_PICKAXE", "_AXE", "_SHOVEL", "TRIDENT")
    val HANDLED_CAUSES =
        arrayListOf(EntityDamageEvent.DamageCause.ENTITY_ATTACK, EntityDamageEvent.DamageCause.PROJECTILE)

    fun enable() {
        listen<EntityDamageEvent> {
            if (it.entity is Player) {
                val player = it.entity as Player
                val cause = it.cause
                if (player.isInFight()) {
                    val duel = Data.duelFromPlayer(player)
                    if (duel.state == GameState.RUNNING) {
                        if (duel.kit.info.specials.contains(Specials.INVINICIBLE)) {
                            it.isCancelled = true
                            return@listen
                        }
                        if (duel.kit.info.specials.contains(Specials.NODAMAGE))
                            it.damage = 0.0
                        if (!HANDLED_CAUSES.contains(cause)) {
                            if (player.health - it.damage <= 0.0) {
                                it.isCancelled = true
                                playerDied(duel, player, cause)
                                return@listen
                            }
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

        listen<EntityDamageByEntityEvent>(priority = EventPriority.HIGHEST) {
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
                                    damage = getFinalDamage(kitMap[duel.kit]!!, damage, damager.inventory.itemInMainHand.type)
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

                        if (player.health - damage <= 0.0) {
                            it.isCancelled = true
                            playerDied(duel, player, it.cause)
                        }
                    }
                }
            } else {
                it.isCancelled = true
            }
        }
    }

    fun playerDied(duel: Duel, player: Player, cause: EntityDamageEvent.DamageCause) {
        duel.playerDied(player, getDeathMessageDE(duel, player, cause), getDeathMessageEN(duel, player, cause))

        if (duel.lastAttackerOfPlayer[player] != null)
            PlayerStats.get(duel.lastAttackerOfPlayer[player]!!).addKill()
    }

    fun getFinalDamage(kit: Kit, damage: Double, material: Material): Double {
        val itemName = material.name
        var finalDamage = damage


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

    fun getDeathMessageDE(duel: Duel, player: Player, damageCause: EntityDamageEvent.DamageCause): String {
        var deathMessage: String

        when (damageCause) {
            EntityDamageEvent.DamageCause.FALL ->
                deathMessage =
                    "${duel.teamColor(player)}${player.name} ${KColors.GRAY}ist an Fallschaden gestorben."
            EntityDamageEvent.DamageCause.FIRE_TICK ->
                deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}ist verbrannt."
            EntityDamageEvent.DamageCause.LAVA ->
                deathMessage =
                    "${duel.teamColor(player)}${player.name} ${KColors.GRAY}hat versucht in Lava zu schwimmen."
            EntityDamageEvent.DamageCause.SUFFOCATION ->
                deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}ist erstickt."
            EntityDamageEvent.DamageCause.ENTITY_ATTACK -> {
                val killer = duel.lastAttackerOfPlayer[player]!!
                deathMessage ="${duel.teamColor(player)}${player.name} ${KColors.GRAY}wurde von ${duel.teamColor(killer)}${killer.name} ${KColors.GRAY}getötet."
            }
            EntityDamageEvent.DamageCause.PROJECTILE -> {
                val killer = duel.lastAttackerOfPlayer[player]!!
                deathMessage ="${duel.teamColor(player)}${player.name} ${KColors.GRAY}wurde von ${duel.teamColor(killer)}${killer.name} ${KColors.GRAY}erschossen."
            }
            else -> deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}ist gestorben."
        }
        return deathMessage
    }

    fun getDeathMessageEN(duel: Duel, player: Player, damageCause: EntityDamageEvent.DamageCause): String {
        var deathMessage: String
        when (damageCause) {
            EntityDamageEvent.DamageCause.FALL ->
                deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}fell too far."
            EntityDamageEvent.DamageCause.FIRE_TICK ->
                deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}burned to death."
            EntityDamageEvent.DamageCause.LAVA ->
                deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}tried to swim in lava."
            EntityDamageEvent.DamageCause.SUFFOCATION ->
                deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}suffocated."
            EntityDamageEvent.DamageCause.ENTITY_ATTACK -> {
                val killer = duel.lastAttackerOfPlayer[player]!!
                deathMessage ="${duel.teamColor(player)}${player.name} ${KColors.GRAY}was killed by ${duel.teamColor(killer)}${killer.name}${KColors.GRAY}."
            }
            EntityDamageEvent.DamageCause.PROJECTILE -> {
                val killer = duel.lastAttackerOfPlayer[player]!!
                deathMessage ="${duel.teamColor(player)}${player.name} ${KColors.GRAY}was shot by ${duel.teamColor(killer)}${killer.name}${KColors.GRAY}."
            }
            else ->
                deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}died."
        }
        return deathMessage
    }

}