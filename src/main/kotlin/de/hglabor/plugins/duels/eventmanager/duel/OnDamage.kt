package de.hglabor.plugins.duels.eventmanager.duel

import de.hglabor.plugins.duels.data.DataHolder
import de.hglabor.plugins.duels.data.PlayerStats
import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.duel.GameState
import de.hglabor.plugins.duels.kits.Kits.Companion.info
import de.hglabor.plugins.duels.kits.Specials
import de.hglabor.plugins.duels.soupsimulator.Soupsim.isInSoupsimulator
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.runnables.async
import org.bukkit.Bukkit
import org.bukkit.entity.Arrow
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
                        if (duel.kit.info.specials.contains(Specials.NODAMAGE))
                            it.damage = 0.0

                        if (it.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK &&
                            it.cause != EntityDamageEvent.DamageCause.PROJECTILE
                        )
                            if (player.health - it.damage <= 0.0) {
                                it.isCancelled = true
                                duel.playerDied(
                                    player,
                                    getDeathMessageDE(duel, player, player.lastDamageCause!!.cause),
                                    getDeathMessageEN(duel, player, player.lastDamageCause!!.cause)
                                )

                                if (duel.lastAttackerOfPlayer[player] != null) {
                                    val killerStats = PlayerStats.get(duel.lastAttackerOfPlayer[player]!!)
                                    killerStats.addKill()
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
                if (it.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    if (it.damager is Player) {
                        val itemName: String = player.inventory.itemInMainHand.type.name

                        if (player.isInFight() && (it.damager as Player).isInFight()) {
                            val damager = it.damager as Player
                            var damage = it.damage
                            val duel = Data.duelFromPlayer(player)

                            if (duel.state == GameState.STARTING)
                                it.isCancelled = true

                            if (duel.state == GameState.RUNNING) {
                                if (!duel.kit.info.specials.contains(Specials.HITCOOLDOWN)) {
                                    if (itemName.endsWith("_PICKAXE") || itemName.endsWith("_AXE") || itemName.endsWith(
                                            "_SHOVEL"
                                        ) || itemName.endsWith("TRIDENT")
                                    ) {
                                        damage = it.damage * 0.25

                                    } else if (itemName.endsWith("_SWORD")) {
                                        damage = it.damage * 0.5
                                    }

                                    if (duel.kit.info.specials.contains(Specials.NODAMAGE))
                                        damage = 0.0

                                    if (!it.isCancelled) {
                                        duel.hits[damager] = duel.hits[it.damager]!! + 1

                                        duel.lastHitOfPlayer[damager] = player
                                        duel.lastAttackerOfPlayer[player] = damager

                                        duel.currentCombo[damager] = duel.currentCombo[it.damager]!! + 1
                                        duel.currentCombo[player] = 0
                                        if (duel.longestCombo[damager]!! < duel.currentCombo[damager]!!) {
                                            duel.longestCombo[damager] = duel.currentCombo[damager]!!
                                        }

                                        async { DataHolder.playerStats[player]?.addTotalHit() }
                                    }

                                    it.damage = damage

                                    if (player.health - damage <= 0.0) {
                                        it.isCancelled = true
                                        duel.playerDied(
                                            player,
                                            getDeathMessageDE(duel, player, player.lastDamageCause!!.cause),
                                            getDeathMessageEN(duel, player, player.lastDamageCause!!.cause)
                                        )

                                        if (duel.lastAttackerOfPlayer[player] != null) {
                                            val killerStats = PlayerStats.get(duel.lastAttackerOfPlayer[player]!!)
                                            killerStats.addKill()
                                        }
                                    }
                                } else {
                                    it.isCancelled = true
                                }
                            } else {
                                it.isCancelled = true
                            }
                        }
                    }

                } else if (it.cause == EntityDamageEvent.DamageCause.PROJECTILE) {
                    val arrow = it.damager as Arrow
                    if (arrow.shooter is Player) {
                        val damager = arrow.shooter as Player
                        if (player.isInFight() && damager.isInFight()) {
                            val duel = Data.duelFromPlayer(player)
                            val damage = it.damage * 0.67
                            it.damage = damage

                            if (player.health - damage <= 0.0) {
                                it.isCancelled = true
                                duel.playerDied(
                                    player,
                                    "${duel.teamColor(player)}${player.name} ${KColors.GRAY}wurde von ${
                                        duel.teamColor(
                                            damager
                                        )
                                    }${damager.name} ${KColors.GRAY}erschossen",
                                    "${duel.teamColor(player)}${player.name} ${KColors.GRAY}was shot by ${
                                        duel.teamColor(
                                            damager
                                        )
                                    }${damager.name} ${KColors.GRAY}"
                                )

                                val killerStats = PlayerStats.get(damager)
                                killerStats.addKill()
                            }
                        }
                    }
                }
            }

        }
    }

    fun getDeathMessageDE(duel: Duel, player: Player, damageCause: EntityDamageEvent.DamageCause): String {
        var deathMessage: String
        if (damageCause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            if (duel.lastAttackerOfPlayer[player] != null) {
                deathMessage =
                    "${duel.teamColor(player)}${player.name} ${KColors.GRAY}wurde von ${duel.teamColor(player)}${duel.lastAttackerOfPlayer[player]?.name} ${KColors.GRAY}getötet."
                val killerStats = PlayerStats.get(duel.lastAttackerOfPlayer[player]!!)
                killerStats.addKill()
                return deathMessage
            }
        }
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
            EntityDamageEvent.DamageCause.PROJECTILE ->
                deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}wurde erschossen xd"
            else ->
                deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}ist gestorben."
        }
        return deathMessage
    }

    fun getDeathMessageEN(duel: Duel, player: Player, damageCause: EntityDamageEvent.DamageCause): String {
        var deathMessage: String
        if (damageCause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            if (duel.lastAttackerOfPlayer[player] != null) {
                deathMessage =
                    "${duel.teamColor(player)}${player.name} ${KColors.GRAY}was killed by ${duel.teamColor(duel.lastAttackerOfPlayer[player]!!)}${duel.lastAttackerOfPlayer[player]?.name}${KColors.GRAY}."
                return deathMessage
            }
        }
        when (damageCause) {
            EntityDamageEvent.DamageCause.FALL ->
                deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}fell too far."
            EntityDamageEvent.DamageCause.FIRE_TICK ->
                deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}burned to death."
            EntityDamageEvent.DamageCause.LAVA ->
                deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}tried to swim in lava."
            EntityDamageEvent.DamageCause.SUFFOCATION ->
                deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}suffocated."
            EntityDamageEvent.DamageCause.PROJECTILE ->
                deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}was shot xd"
            else ->
                deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}died."
        }
        return deathMessage
    }
}