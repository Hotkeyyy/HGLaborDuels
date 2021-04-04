package de.hglabor.plugins.duels.tournament

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.duel.GameState
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.utils.Localization
import de.hglabor.plugins.duels.utils.sendMsg
import de.hglabor.plugins.duels.party.Party
import de.hglabor.plugins.duels.party.Partys.isInParty
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.reset
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.async
import net.axay.kspigot.runnables.task
import net.axay.kspigot.runnables.taskRunLater
import org.bukkit.Sound
import org.bukkit.entity.Player

class Tournament {
    companion object {
        fun createPublic(host: Player, kit: AbstractKit) {
            val tournament = Tournament()
            tournament.host = host
            tournament.type = TournamentType.PUBLIC
            tournament.kit = kit
            tournament.announce(true)
            Tournaments.publicTournament = tournament
            tournament.startTimer()
        }

        /*fun createParty(party: Party, kit: Kits) {
            val tournament = Tournament()
            tournament.host = party.leader
            tournament.type = TournamentType.PARTY
            tournament.kit = kit
        }*/
    }

    lateinit var host: Player
    lateinit var kit: AbstractKit
    lateinit var type: TournamentType
    var state = GameState.COUNTDOWN
    val players = arrayListOf<Player>()
    val teams = arrayListOf<ArrayList<Player>>()
    val fightingTeams = arrayListOf<ArrayList<Player>>()
    val duels = arrayListOf<Duel>()

    val teamSize = 1

    var timeToStart = 120

    fun announce(ifHost: Boolean) {
        val min = timeToStart / 60
        val sec = timeToStart % 60

        async {
            onlinePlayers.forEach {
                if (ifHost) {
                    it.playSound(it.location, Sound.ENTITY_ENDER_DRAGON_GROWL, 3f, 3f)
                    it.sendMsg("tournament.hosted", mutableMapOf("hostName" to host.name, "kit" to kit.name))
                }
                if (it.localization("de"))
                    it.sendMessage("${Localization.PREFIX}Das Turnier startet in ${KColors.MEDIUMPURPLE}$min${KColors.DARKGRAY}:${KColors.MEDIUMPURPLE}$sec ${(if (min == 1) "Minute" else "Minuten")}§7.")
                else
                    it.sendMessage("${Localization.PREFIX}The tournament will start in ${KColors.MEDIUMPURPLE}$min${KColors.DARKGRAY}:${KColors.MEDIUMPURPLE}$sec ${(if (min == 1) "minute" else "minutes")}§7.")
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
                        state = GameState.INGAME
                        startDuels()
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
                players.add(player)
                teams.add(arrayListOf(player))
                async {
                    onlinePlayers.forEach { players ->
                        players.sendMsg("tournament.playerJoined", mutableMapOf("playerName" to player.name))
                    }
                }
            } else {
                players.addAll(party.players)
                teams.add(party.players)
                async {
                    onlinePlayers.forEach { players ->
                        players.sendMsg("tournament.partyJoined", mutableMapOf("leaderName" to player.name))
                    }
                }
            }
        } else {
            players.add(player)
            teams.add(arrayListOf(player))
            async {
                onlinePlayers.forEach { players ->
                    players.sendMsg("tournament.playerJoined", mutableMapOf("playerName" to player.name))
                }
            }
        }
    }

    fun leave(player: Player) {
        for (team in teams) {
            if (team.contains(player))
                teams.remove(team)
            team.remove(player)
        }
        async {
            onlinePlayers.forEach { players ->
                players.sendMsg("tournament.playerLeft", mutableMapOf("playerName" to player.name))
            }
        }
        players.remove(player)
    }

    private fun startDuels() {
        val teamlist = arrayListOf<ArrayList<Player>>()
        teamlist.addAll(teams)

        val duels = teams.size / 2.0
        val tasks: Long
        if (duels - duels.toInt() != 0.0)
            tasks = teams.size / 2L + 1
        else
            tasks = teams.size / 2L

        task(
            sync = true,
            delay = 0,
            period = 5,
            howOften = tasks
        ) {
            if (teamlist.size > 1) {
                val teamOne = teamlist.random()
                teamlist.remove(teamOne)
                fightingTeams.add(teamOne)

                val teamTwo = teamlist.random()
                teamlist.remove(teamTwo)
                fightingTeams.add(teamTwo)

                Duel.createTournament(teamOne, teamTwo, kit, this)
            } else {
                async {
                    teamlist.forEach {
                        it.forEach { players ->
                            players.sendMsg("tournament.noEnemyFound")
                        }
                        teamlist.remove(it)
                    }
                }
            }
        }
    }

    fun duelEnded(duel: Duel) {
        duel.loser.forEach { players.remove(it) }
        teams.remove(duel.loser)
        fightingTeams.remove(duel.loser)
        fightingTeams.remove(duel.winner)
        duels.remove(duel)
        taskRunLater(
            sync = true,
            delay = 60
        ) {
            if (fightingTeams.isEmpty()) {
                roundEnded()
                async {
                    onlinePlayers.forEach { players ->
                        players.sendMsg("tournament.roundEnded")
                    }
                }
            }
        }
    }

    fun roundEnded() {
        if (teams.size == 1) {
            endTournament()
            return

        } else if (teams.size > 1) {
            async {
                onlinePlayers.forEach { players ->
                    players.sendMsg("tournament.nextRoundStarting")
                }
            }
            taskRunLater(60L, false) { startDuels() }
        }
    }

    private fun endTournament() {
        state = GameState.ENDED
        var winners = ""
        for (i in 0 until teams.first().size) {
            winners += teams.first()[i].name
            if (i < teams.first().size)
                winners += ", "
        }
        async {
            onlinePlayers.forEach { players ->
                players.sendMsg("tournament.teamWins", mutableMapOf("winner" to winners))
            }
        }
        Tournaments.publicTournament = null

        players.forEach {
            it.reset()
        }
    }

    fun sendMessage(message: String) {
        if (type == TournamentType.PUBLIC) {
            async {
                onlinePlayers.forEach {
                    it.sendMessage(message)
                }
            }
        } else {
            async {
                players.forEach {
                    it.sendMessage(message)
                }
            }
        }
    }
}
