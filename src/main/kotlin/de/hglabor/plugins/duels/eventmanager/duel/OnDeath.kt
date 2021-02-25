package de.hglabor.plugins.duels.eventmanager.duel

import de.hglabor.plugins.duels.data.PlayerStats
import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.runnables.task
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent

object OnDeath {

    fun enable() {
        listen<PlayerDeathEvent> {
            val player = it.entity
            player.world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true)
            val cause = player.lastDamageCause!!.cause
            if (player.isInFight()) {
                val duel = Data.duelFromPlayer(player)
                playerDied(duel, player, cause)
            }
        }

        listen<PlayerRespawnEvent> {
            val player = it.player
            task(true, 1) {
                if (Data.duelFromSpec.containsKey(player)) {
                    val duel = Data.duelFromSpec[player]!!
                    val arena = duel.arena
                    duel.addSpectator(player, false)
                    val spawnOne = arena.spawn1Loc
                    val spawnTwo = arena.spawn2Loc
                    val x = (spawnOne.x + spawnTwo.x) / 2
                    val y = spawnOne.y + 3
                    val z = (spawnOne.z + spawnTwo.z) / 2
                    val centerLoc = Location(Bukkit.getWorld("FightWorld"), x, y, z)
                    player.teleport(centerLoc)

                }
            }
        }
    }

    fun playerDied(duel: Duel, player: Player, cause: EntityDamageEvent.DamageCause) {
        duel.playerDied(player, getDeathMessageDE(duel, player, cause), getDeathMessageEN(duel, player, cause))

        if (duel.lastAttackerOfPlayer[player] != null)
            PlayerStats.get(duel.lastAttackerOfPlayer[player]!!).addKill()
    }



    fun getDeathMessageDE(duel: Duel, player: Player, damageCause: EntityDamageEvent.DamageCause): String {
        val deathMessage: String

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
        val deathMessage: String
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