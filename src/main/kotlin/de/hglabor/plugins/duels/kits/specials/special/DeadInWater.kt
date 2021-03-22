package de.hglabor.plugins.duels.kits.specials.special

import de.hglabor.plugins.duels.duel.GameState
import de.hglabor.plugins.duels.events.events.duel.DuelDeathReason
import de.hglabor.plugins.duels.events.events.duel.PlayerDeathInDuelEvent
import de.hglabor.plugins.duels.kits.specials.Specials
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.isFeetInWater
import org.bukkit.Bukkit
import org.bukkit.event.player.PlayerMoveEvent

object DeadInWater {
    init {
        listen<PlayerMoveEvent> {
            val player = it.player
            if (player.isInFight()) {
                val duel = Data.duelFromID[Data.duelIDFromPlayer[player]]
                if (duel?.state == GameState.COUNTDOWN)
                    it.isCancelled = true
                if (duel?.kit?.specials?.contains(Specials.DEADINWATER) == true) {
                    if (duel.state == GameState.RUNNING) {
                        if (player.isFeetInWater) {
                            Bukkit.getPluginManager().callEvent(PlayerDeathInDuelEvent(player, duel, DuelDeathReason.WATER))
                        }
                    }
                } else if (duel?.kit?.specials?.contains(Specials.JUMPANDRUN) == true) {
                    if (duel.state == GameState.RUNNING) {
                        if (player.isFeetInWater) {
                            Bukkit.getPluginManager().callEvent(PlayerDeathInDuelEvent(player, duel, DuelDeathReason.WATER))
                        }
                    }
                }
            }
        }
    }
}