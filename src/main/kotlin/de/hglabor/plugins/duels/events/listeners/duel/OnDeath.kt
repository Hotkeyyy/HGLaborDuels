package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.events.events.duel.PlayerDeathInDuelEvent
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.event.listen
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.event.entity.PlayerDeathEvent

object OnDeath {

    fun enable() {
        listen<PlayerDeathEvent> {
            val player = it.entity
            player.world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true)
            val cause = player.lastDamageCause!!.cause
            if (player.isInFight()) {
                val duel = Data.duelFromPlayer(player)
                Bukkit.getPluginManager().callEvent(PlayerDeathInDuelEvent(player, duel, null, cause))
            }
        }
    }
}