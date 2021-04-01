package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.duel.GameState
import de.hglabor.plugins.duels.player.DuelsPlayer
import net.axay.kspigot.event.listen
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEvent

object OnProjectileLaunch {
    init {
        listen<ProjectileLaunchEvent> {
            if (it.entity.shooter is Player) {
                val projectile = it.entity
                val shooter = projectile.shooter as Player
                val duelsPlayer = DuelsPlayer.get(shooter)
                val duel = duelsPlayer.currentDuel() ?: return@listen
                it.isCancelled = duel.state != GameState.INGAME
            }
        }

        listen<PlayerInteractEvent> {
            val player = it.player
            val duelsPlayer = DuelsPlayer.get(player)
            val duel = duelsPlayer.currentDuel() ?: return@listen
            it.isCancelled = duel.state != GameState.INGAME
        }
    }
}