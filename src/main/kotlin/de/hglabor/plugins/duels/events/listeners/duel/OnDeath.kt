package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.events.events.duel.PlayerDeathInDuelEvent
import de.hglabor.plugins.duels.player.DuelsPlayer
import net.axay.kspigot.event.listen
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.event.entity.PlayerDeathEvent

object OnDeath {

    init {
        listen<PlayerDeathEvent> {
            val player = it.entity
            val duelsPlayer = DuelsPlayer.get(player)
            player.world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true)
            val cause = player.lastDamageCause!!.cause
            if (duelsPlayer.isInFight()) {
                val duel = duelsPlayer.currentDuel() ?: return@listen
                Bukkit.getPluginManager().callEvent(PlayerDeathInDuelEvent(player, duel, null, cause))
            }
        }
    }
}