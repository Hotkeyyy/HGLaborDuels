package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.events.events.duel.DuelDeathReason
import de.hglabor.plugins.duels.events.events.duel.PlayerDeathInDuelEvent
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.reset
import net.axay.kspigot.event.listen
import net.axay.kspigot.runnables.task
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.Location
import org.bukkit.event.entity.EntityDamageEvent

object OnDeathInDuel {
    init {
        listen<PlayerDeathInDuelEvent> {
            val player = it.player
            val duel = it.duel

            if (it.duelDeathReason != DuelDeathReason.QUIT) {
                if (duel.kit.allowsRespawn) {
                    if (duel.teamOne.contains(player)) {
                        player.teleport(duel.arena.spawn1Loc)
                        duel.direction(player, duel.arena.spawn2Loc)
                    } else {
                        player.teleport(duel.arena.spawn2Loc)
                        duel.direction(player, duel.arena.spawn1Loc)
                    }
                    return@listen
                }
            }

            Data.duelOfSpec[player] = duel
            Data.inFight.remove(player)
            duel.alivePlayers.remove(player)
            val newAlivePlayers = duel.alivePlayers
            duel.alivePlayers = newAlivePlayers
            duel.savePlayerdata(player)
            Kits.removeCooldown(player)

            if (it.duelDeathReason != null) {
                duel.sendMsg("death.duel.${it.duelDeathReason.toString().toLowerCase()}",
                    mutableMapOf("teamColor" to "${duel.teamColor(player)}", "playerName" to player.name))

                if (it.duelDeathReason == DuelDeathReason.QUIT)
                    player.reset()

            } else if (it.bukkitDeathReason != null) {
                player.world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true)
                if (it.bukkitDeathReason == EntityDamageEvent.DamageCause.PROJECTILE || it.bukkitDeathReason == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    val killer = duel.lastAttackerOfPlayer[player]
                    if (killer != null) {
                        duel.sendMsg("death.bukkit.${it.bukkitDeathReason.toString().toLowerCase()}",
                            mutableMapOf("teamColor" to "${duel.teamColor(player)}", "playerName" to player.name,
                            "killerTeamColor" to "${duel.teamColor(killer)}", "killerName" to killer.name))
                    } else {
                        duel.sendMsg("death.bukkit.${it.bukkitDeathReason.toString().toLowerCase()}",
                            mutableMapOf("teamColor" to "${duel.teamColor(player)}", "playerName" to player.name))
                    }
                } else {
                    duel.sendMsg("death.bukkit.${it.bukkitDeathReason.toString().toLowerCase()}",
                        mutableMapOf("teamColor" to "${duel.teamColor(player)}", "playerName" to player.name))
                }
            }

            task(true, 2) {
                if (Data.duelOfSpec.containsKey(player)) {
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

            if (duel.ifTeamDied(duel.getTeam(player))) {
                duel.loser = duel.getTeam(player)
                duel.winner = duel.getOtherTeam(player)
                duel.stop()
            }
        }
    }
}