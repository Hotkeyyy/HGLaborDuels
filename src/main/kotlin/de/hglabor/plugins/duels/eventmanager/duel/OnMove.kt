package de.hglabor.plugins.duels.eventmanager.duel

import de.hglabor.plugins.duels.duel.GameState
import de.hglabor.plugins.duels.kits.Kits.Companion.info
import de.hglabor.plugins.duels.kits.Specials
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.isFeetInWater
import org.bukkit.event.player.PlayerMoveEvent

object OnMove {
    fun enable() {
        listen<PlayerMoveEvent> {
            val player = it.player
            if (player.isInFight()) {
                val duel = Data.duelFromID[Data.duelIDFromPlayer[player]]
                if (duel?.state == GameState.COUNTDOWN)
                    it.isCancelled = true
                if (duel?.kit?.info?.specials?.contains(Specials.DEADINWATER) == true) {
                    if (duel.state == GameState.RUNNING) {
                        if (player.isFeetInWater) {
                            duel.playerDied(player,  "${duel.teamColor(player)}${player.name} ${KColors.GRAY}ist gestorben.", "${duel.teamColor(player)}${player.name} ${KColors.GRAY}died.")
                        }
                    }
                } else if(duel?.kit?.info?.specials?.contains(Specials.JUMPANDRUN) == true) {
                    if (duel.state == GameState.RUNNING) {
                        if (player.isFeetInWater) {
                            if (duel.teamOne.contains(player)) {
                                player.teleport(duel.arena.spawn1Loc)
                                duel.direction(player, duel.arena.spawn2Loc)
                            } else {
                                player.teleport(duel.arena.spawn2Loc)
                                duel.direction(player, duel.arena.spawn1Loc)
                            }
                        }
                    }
                }
            }
        }
    }
}