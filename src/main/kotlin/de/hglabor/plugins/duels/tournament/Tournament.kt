package de.hglabor.plugins.duels.tournament

import de.hglabor.plugins.duels.duel.TournamentDuel
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.party.Party
import de.hglabor.plugins.duels.party.Partys.isInParty
import de.hglabor.plugins.duels.team.Team
import de.hglabor.plugins.duels.team.TeamColor
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.Localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.reset
import de.hglabor.plugins.duels.utils.sendMsg
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.async
import net.axay.kspigot.runnables.task
import org.bukkit.Sound
import org.bukkit.entity.Player

class Tournament(val host: Player, val teamSize: Int, val kit: AbstractKit) {
    companion object {
        enum class TournamentType { PARTY, PUBLIC }
        var publicTournament: Tournament? = null

        fun create(host: Player, kit: AbstractKit) {
            val tournament = Tournament(host, 1, kit)
            tournament.announceTimer(true)
            tournament.startTimer()
        }
    }

    var state = Data.GameState.COUNTDOWN
    val teams = mutableListOf<Team>()
    val fightingTeams = mutableListOf<Team>()
    val players = mutableMapOf<Player, Team>()
    val duels = mutableListOf<TournamentDuel>()
    var timeToStart = 120

    fun announceTimer(isCreation: Boolean) {
        val min = timeToStart / 60
        val sec = timeToStart % 60

        async {
            onlinePlayers.forEach {
                it.playSound(it.location, Sound.ENTITY_ENDER_DRAGON_GROWL, 3f, 3f)
                if (isCreation) {
                    it.sendMsg("tournament.hosted", mutableMapOf("hostName" to host.name, "kit" to kit.name))
                }
                if (it.localization("de")) {
                    it.sendMessage("${Localization.PREFIX}Das Turnier startet in ${KColors.MEDIUMPURPLE}$min${KColors.DARKGRAY}:${KColors.MEDIUMPURPLE}$sec ${(if (min == 1) "Minute" else "Minuten")}§7.")
                } else {
                    it.sendMessage("${Localization.PREFIX}The tournament will start in ${KColors.MEDIUMPURPLE}$min${KColors.DARKGRAY}:${KColors.MEDIUMPURPLE}$sec ${(if (min == 1) "minute" else "minutes")}§7.")
                }
            }
        }
    }

    fun startTimer() {
        task(false, 20, 20, Long.MAX_VALUE) {
            if (teams.size >= 2) {
                timeToStart--
                if (timeToStart % 30 == 0) {
                    val min = timeToStart / 60
                    val sec = timeToStart % 60

                    if (timeToStart == 0) {
                        async {
                            onlinePlayers.forEach { players ->
                                players.sendMsg("tournament.start", mutableMapOf("teamCount" to "${teams.size}"))
                            }
                        }
                        state = Data.GameState.INGAME
                        prepareDuels()
                        it.cancel()
                        return@task
                    }
                    onlinePlayers.forEach { players ->
                        if (players.localization("de"))
                            players.sendMessage("${Localization.PREFIX}Das Turnier startet in ${KColors.MEDIUMPURPLE}$min${KColors.DARKGRAY}:${KColors.MEDIUMPURPLE}$sec ${(if (min == 1) "Minute" else "Minuten")}§7.")
                        else
                            players.sendMessage("${Localization.PREFIX}The tournament will start in ${KColors.MEDIUMPURPLE}$min${KColors.DARKGRAY}:${KColors.MEDIUMPURPLE}$sec ${(if (min == 1) "minute" else "minutes")}§7.")
                    }
                }
            }
        }
    }

    fun join(player: Player) {
        if (player.isInParty()) {
            val party = Party.get(player)
            if (party!!.players.size > teamSize || party.players.size == 1) {
                party.delete()
                val team = Team(player, TeamColor.teamColors.random())
                players[player] = team
                teams.add(team)
                async {
                    onlinePlayers.forEach { players ->
                        players.sendMsg("tournament.playerJoined", mutableMapOf("playerName" to player.name))
                    }
                }
            } else {
                val team = Team(party.players, TeamColor.teamColors.random())
                party.players.forEach { players[it] = team }
                teams.add(team)
                async {
                    onlinePlayers.forEach { players ->
                        players.sendMsg("tournament.partyJoined", mutableMapOf("leaderName" to player.name))
                    }
                }
            }
        } else {
            val team = Team(player, TeamColor.teamColors.random())
            players[player] = team
            async {
                onlinePlayers.forEach { players ->
                    players.sendMsg("tournament.playerJoined", mutableMapOf("playerName" to player.name))
                }
            }
        }
    }

    fun leave(player: Player) {
        if (players.containsKey(player)) {
            val team = players[player]!!
            team.members.remove(player)
            if (team.members.size == 0) {
                teams.remove(team)
            }
            async {
                onlinePlayers.forEach { players ->
                    players.sendMsg("tournament.playerLeft", mutableMapOf("playerName" to player.name))
                }
            }
            players.remove(player)
        }
    }

    fun prepareDuels() {
        val teamList = ArrayList(teams)
        val duelCount = teams.size / 2.0 + 1
        val tasks = if (duelCount - duelCount.toInt() != 0.0) duelCount.toLong() + 1 else duelCount.toLong()

        task(false, 0, 10, tasks) {
            players.keys.forEach { player -> player.sendTitle("Preparing duels", "Please wait...", 5, 21, 4) }

            if (teamList.size > 1) {
                val teamOne = teamList.random()
                teamList.remove(teamOne)
                val teamTwo = teamList.random()
                teamList.remove(teamTwo)

                TournamentDuel(this, teamOne, teamTwo, kit)
            } else if (teamList.size == 1) {
                teamList.forEach { team ->
                    team.members.forEach { players ->
                        players.sendMsg("tournament.noEnemyFound")
                    }
                    teamList.remove(team)
                }
            } else if (teamList.size == 0) {
                startDuels()
                it.cancel()
            }
        }
    }

    private fun startDuels() {
        for (duel in duels) {
            duel.start()
        }
    }

    fun endTournament() {
        state = Data.GameState.ENDED
        var winners = ""
        for (i in 0 until teams.first().members.size) {
            winners += teams.first().members[i].name
            if (i < teams.first().members.size)
                winners += ", "
        }
        async {
            onlinePlayers.forEach { players ->
                players.sendMsg("tournament.teamWins", mutableMapOf("winner" to winners))
            }
        }
        publicTournament = null

        players.keys.forEach {
            it.reset()
        }
    }
}
