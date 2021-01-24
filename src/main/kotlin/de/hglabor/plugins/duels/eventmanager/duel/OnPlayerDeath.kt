package de.hglabor.plugins.duels.eventmanager.duel

import de.hglabor.plugins.duels.data.PlayerStats
import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent

object OnPlayerDeath {
    fun enable() {
        listen<EntityDamageEvent> {
            if (it.entity is Player) {
                val player = it.entity as Player
                if (player.isInFight()) {
                    if (it.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                        if (player.health - it.damage < 0) {
                            val duel = Data.duelFromPlayer(player)
                            duel.playerDied(player,
                                getDeathMessageDE(duel, player, player.lastDamageCause!!.cause),
                                getDeathMessageEN(duel, player, player.lastDamageCause!!.cause))

                            if (duel.lastAttackerOfPlayer[player] != null) {
                                val killerStats = PlayerStats.get(duel.lastAttackerOfPlayer[player]!!)
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
                deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}wurde von ${duel.teamColor(player)}${duel.lastAttackerOfPlayer[player]?.name} ${KColors.GRAY}getötet."
                val killerStats = PlayerStats.get(duel.lastAttackerOfPlayer[player]!!)
                killerStats.addKill()
                return deathMessage
            }
        }
        when (damageCause) {
            EntityDamageEvent.DamageCause.FALL ->
                deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}ist an Fallschaden gestorben."
            EntityDamageEvent.DamageCause.FIRE_TICK ->
                deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}ist verbrannt."
            EntityDamageEvent.DamageCause.LAVA ->
                deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}hat versucht in Lava zu schwimmen."
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
                deathMessage = "${duel.teamColor(player)}${player.name} ${KColors.GRAY}was killed by ${duel.teamColor(duel.lastAttackerOfPlayer[player]!!)}${duel.lastAttackerOfPlayer[player]?.name}${KColors.GRAY}."
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