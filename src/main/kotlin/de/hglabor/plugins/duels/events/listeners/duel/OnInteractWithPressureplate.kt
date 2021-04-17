package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.kits.specials.Specials
import de.hglabor.plugins.duels.utils.Localization
import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.duels.utils.Data
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
                    if (duel.gameState == Data.GameState.INGAME) {
                        if (it.action == Action.PHYSICAL) {
                            if (it.clickedBlock?.type == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
                                val ownTeam = duel.getTeamOfPlayer(player)
                                duel.sendMsg(Localization.getMessage("duel.win.reachedGoal",
                                    mutableMapOf("teamColor" to "${ownTeam.teamColor.mainColor}", "playerName" to player.name), player))
                                duel.setWinner(ownTeam)
                            }
                        }
                    }
                }
            }
        }
    }
}