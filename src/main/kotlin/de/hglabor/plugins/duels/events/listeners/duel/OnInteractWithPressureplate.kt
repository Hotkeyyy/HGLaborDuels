package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.duel.GameState
import de.hglabor.plugins.duels.kits.specials.Specials
import de.hglabor.plugins.duels.utils.Localization
import de.hglabor.plugins.duels.player.DuelsPlayer
import net.axay.kspigot.event.listen
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

object OnInteractWithPressureplate {
    init {
        listen<PlayerInteractEvent> {
            val player = it.player
            val duelsPlayer = DuelsPlayer.get(player)

            if (duelsPlayer.isInFight()) {
                val duel = duelsPlayer.currentDuel() ?: return@listen
                if (duel.kit.specials.contains(Specials.JUMPANDRUN)) {
                    if (duel.state == GameState.INGAME) {
                        if (it.action == Action.PHYSICAL) {
                            if (it.clickedBlock?.type == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
                                duel.sendMsg(Localization.getMessage("duel.win.reachedGoal",
                                    mutableMapOf("teamColor" to "${duel.teamColor(player)}", "playerName" to player.name), player))
                                duel.winner = duel.getTeam(player)
                                duel.loser = duel.getOtherTeam(player)
                                duel.stop()
                            }
                        }
                    }
                }
            }
        }
    }
}