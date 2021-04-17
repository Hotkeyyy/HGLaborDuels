package de.hglabor.plugins.duels.duel

import de.hglabor.plugins.duels.events.events.duel.TournamentDuelEndEvent
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.team.Team
import de.hglabor.plugins.duels.tournament.Tournament
import org.bukkit.Bukkit

class TournamentDuel(val tournament: Tournament, teamOne: Team, teamTwo: Team, kit: AbstractKit): AbstractDuel(teamOne, teamTwo, kit) {
    init {
        tournament.fightingTeams.add(teamOne)
        tournament.fightingTeams.add(teamTwo)
    }
}