package de.hglabor.plugins.duels.tournament

enum class TournamentType { PARTY, PUBLIC }

object Tournaments {
    var publicTournament: Tournament? = null
}