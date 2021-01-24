package de.hglabor.plugins.duels.tournament

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.duel.GameState
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.kitMap
import de.hglabor.plugins.duels.localization.Localization
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
        fun createPublic(host: Player, kit: Kits) {
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
    lateinit var kit: Kits
    lateinit var type: TournamentType
    var state = GameState.COUNTDOWN
    val players = arrayListOf<Player>()
    val teams = arrayListOf<ArrayList<Player>>()
    val fightingTeams = arrayListOf<ArrayList<Player>>()

    val teamSize = 1

    var timeToStart = 120

    fun announce(ifHost: Boolean) {
        val min = timeToStart / 60
        val sec = timeToStart % 60

        async {
            onlinePlayers.forEach {
                if (ifHost) {
                    it.playSound(it.location, Sound.ENTITY_ENDER_DRAGON_GROWL, 3f, 3f)
                    if (it.localization("de"))
                        it.sendMessage("${Localization.PREFIX}${KColors.DODGERBLUE}${host.name} ${KColors.GRAY}veranstaltet ein ${KColors.MEDIUMPURPLE}${kitMap[kit]?.name} Turnier§7.")
                    else
                        it.sendMessage("${Localization.PREFIX}${KColors.DODGERBLUE}${host.name} ${KColors.GRAY}is hosting a ${KColors.MEDIUMPURPLE}${kitMap[kit]?.name} Tournament§7.")
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
                                if (players.localization("de"))
                                    players.sendMessage("${Localization.PREFIX}Das Turnier hat begonnen. Es nehmen ${KColors.MEDIUMPURPLE}${teams.size} Teams §7teil.")
                                else
                                    players.sendMessage("${Localization.PREFIX}The tournament started. There're ${KColors.MEDIUMPURPLE}${teams.size} Teams §7participating.")
                            }
                        }
                        state = GameState.RUNNING
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
                        if (players.localization("de"))
                            players.sendMessage("${Localization.PREFIX}${KColors.MEDIUMPURPLE}${player.name} §7ist dem Turnier ${KColors.LIGHTGREEN}beigetreten§7.")
                        else
                            players.sendMessage("${Localization.PREFIX}${KColors.MEDIUMPURPLE}${player.name} §7${KColors.LIGHTGREEN}joined §7the tournament.")
                    }
                }
            } else {
                players.addAll(party.players)
                teams.add(party.players)
                async {
                    onlinePlayers.forEach { players ->
                        if (players.localization("de"))
                            players.sendMessage("${Localization.PREFIX}${KColors.MEDIUMPURPLE}${player.name}'s Party §7ist dem Turnier ${KColors.LIGHTGREEN}beigetreten§7.")
                        else
                            players.sendMessage("${Localization.PREFIX}${KColors.MEDIUMPURPLE}${player.name}'s party §7${KColors.LIGHTGREEN}joined §7the tournament.")
                    }
                }
            }
        } else {
            players.add(player)
            teams.add(arrayListOf(player))
            async {
                onlinePlayers.forEach { players ->
                    if (players.localization("de"))
                        players.sendMessage("${Localization.PREFIX}${KColors.MEDIUMPURPLE}${player.name} §7ist dem Turnier ${KColors.LIGHTGREEN}beigetreten§7.")
                    else
                        players.sendMessage("${Localization.PREFIX}${KColors.MEDIUMPURPLE}${player.name} §7${KColors.LIGHTGREEN}joined §7the tournament.")
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
                if (players.localization("de"))
                    players.sendMessage("${Localization.PREFIX}${KColors.MEDIUMPURPLE}${player.name} §7hat das Turnier ${KColors.TOMATO}verlassen§7.")
                else
                    players.sendMessage("${Localization.PREFIX}${KColors.MEDIUMPURPLE}${player.name} §7${KColors.TOMATO}left §7the tournament.")
            }
        }
        players.remove(player)
    }

    fun startDuels() {
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
                            if (players.localization("de"))
                                players.sendMessage("${Localization.PREFIX}Es wurde ${KColors.MEDIUMPURPLE}kein Gegner §7für dein Team gefunden. Damit seid ihr automatisch eine Runde weiter.")
                            else
                                players.sendMessage("${Localization.PREFIX}${KColors.MEDIUMPURPLE}No opponent §7was found for your team. You will be competing in the next round.")
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

        taskRunLater(
            sync = true,
            delay = 60
        ) {
            if (fightingTeams.isEmpty()) {
                roundEnded()
                async {
                    onlinePlayers.forEach { players ->
                        if (players.localization("de"))
                            players.sendMessage("${Localization.PREFIX}Die Runde ist geendet. Es verbleiben ${KColors.MEDIUMPURPLE}${teams.size} Teams§7.")
                        else
                            players.sendMessage("${Localization.PREFIX}The round ended. There're ${KColors.MEDIUMPURPLE}${teams.size} teams §7left.")
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
                    if (players.localization("de"))
                        players.sendMessage("${Localization.PREFIX}Die nächste Runde startet in ${KColors.MEDIUMPURPLE}3 Sekunden§7!")
                    else
                        players.sendMessage("${Localization.PREFIX}The next round starts in ${KColors.MEDIUMPURPLE}3 seconds§7!")
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
                if (players.localization("de"))
                    players.sendMessage("${Localization.PREFIX}Das Team bestehend aus ${KColors.MEDIUMPURPLE}$winners ${KColors.LIGHTGREEN}hat das Turnier gewonnen§7.")
                else
                    players.sendMessage("${Localization.PREFIX}The team consisting of ${KColors.MEDIUMPURPLE}$winners ${KColors.LIGHTGREEN}won the tournament§7!")
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
