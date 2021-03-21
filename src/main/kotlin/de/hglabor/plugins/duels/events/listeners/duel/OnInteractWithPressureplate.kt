package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.data.PlayerStats
import de.hglabor.plugins.duels.duel.GameState
import de.hglabor.plugins.duels.kits.specials.Specials
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

object OnInteractWithPressureplate {
    fun enable() {
        listen<PlayerInteractEvent> {
            val player = it.player
            if (player.isInFight()) {
                val duel = Data.duelFromPlayer(player)
                if (duel.kit.specials.contains(Specials.JUMPANDRUN)) {
                    if (duel.state == GameState.RUNNING) {
                        if (it.action == Action.PHYSICAL) {
                            val block = it.clickedBlock!!

                            //GOAL
                            if (block.type == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
                                duel.sendMsg(Localization.INSTANCE.getMessage("duel.win.reachedGoal",
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