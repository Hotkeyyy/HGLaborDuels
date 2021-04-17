package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.events.events.duel.TournamentDuelEndEvent
import de.hglabor.plugins.duels.utils.sendMsg
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.async
import net.axay.kspigot.runnables.taskRunLater

object OnTournamentDuelEnd {
    init {
        listen<TournamentDuelEndEvent> {
            val duel = it.duel
            val tournament = it.duel.tournament
            tournament.duels.remove(duel)
            tournament.fightingTeams.remove(duel.teamOne)
            tournament.fightingTeams.remove(duel.teamTwo)
            tournament.teams.add(duel.getWinner())
            duel.getLoser().members.forEach { member -> tournament.players.remove(member) }

            tournament.duels.remove(duel)
            if (tournament.fightingTeams.isEmpty()) {
                taskRunLater(sync = true, delay = 60) {
                    if (tournament.teams.size == 1) {
                        tournament.endTournament()
                        return@taskRunLater

                    } else if (tournament.teams.size > 1) {
                        async {
                            onlinePlayers.forEach { players ->
                                players.sendMsg("tournament.nextRoundStarting")
                            }
                        }
                        taskRunLater(60L, false) { tournament.prepareDuels() }
                    }
                }
            }
        }
    }
}