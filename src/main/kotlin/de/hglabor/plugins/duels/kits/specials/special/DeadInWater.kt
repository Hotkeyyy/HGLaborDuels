package de.hglabor.plugins.duels.kits.specials.special

import de.hglabor.plugins.duels.duel.GameState
import de.hglabor.plugins.duels.events.events.duel.DuelDeathReason
import de.hglabor.plugins.duels.events.events.duel.PlayerDeathInDuelEvent
import de.hglabor.plugins.duels.kits.specials.Specials
import de.hglabor.plugins.duels.player.DuelsPlayer
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.isFeetInWater
import org.bukkit.Bukkit
import org.bukkit.event.player.PlayerMoveEvent

object DeadInWater {
    init {
        listen<PlayerMoveEvent> {
            val player = it.player
            val duelsPlayer = DuelsPlayer.get(player)
            val duel = duelsPlayer.currentDuel() ?: return@listen

            if (duel.state == GameState.COUNTDOWN) {
                it.isCancelled = true
                return@listen
            }

            if (duel.kit.hasSpecial(Specials.DEADINWATER) || duel.kit.hasSpecial(Specials.JUMPANDRUN)) {
                if (duel.state == GameState.INGAME) {
                    if (player.isFeetInWater) {
                        Bukkit.getPluginManager().callEvent(PlayerDeathInDuelEvent(player, duel, DuelDeathReason.WATER))
                    }
                }
            }
        }
    }
}