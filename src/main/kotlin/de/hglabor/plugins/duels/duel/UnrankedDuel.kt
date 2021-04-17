package de.hglabor.plugins.duels.duel

import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.kit.Random
import de.hglabor.plugins.duels.party.Party
import de.hglabor.plugins.duels.team.Team
import de.hglabor.plugins.duels.team.TeamColor
import org.bukkit.entity.Player

class UnrankedDuel(teamOne: Team, teamTwo: Team, kit: AbstractKit) : AbstractDuel(teamOne, teamTwo, kit) {
    companion object {
        fun create(teamOneLeader: Player, teamTwoLeader: Player, abstractKit: AbstractKit): UnrankedDuel {
            val teamOnePlayers: MutableList<Player> = Party.get(teamOneLeader)?.players ?: mutableListOf(teamOneLeader)
            val teamTwoPlayers: MutableList<Player> = Party.get(teamTwoLeader)?.players ?: mutableListOf(teamTwoLeader)
            val kit = if (abstractKit == Random) Kits.random() else abstractKit
            return UnrankedDuel(Team(teamOnePlayers, TeamColor.PINK), Team(teamTwoPlayers, TeamColor.BLUE), kit)
        }

        fun create(teamOnePlayers: MutableList<Player>, teamTwoPlayers: MutableList<Player>, abstractKit: AbstractKit): UnrankedDuel {
            val kit = if (abstractKit == Random) Kits.random() else abstractKit
            return UnrankedDuel(Team(teamOnePlayers, TeamColor.PINK), Team(teamTwoPlayers, TeamColor.BLUE), kit)
        }
    }
}