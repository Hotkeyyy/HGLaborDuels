package de.hglabor.plugins.duels.tournament

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.kitMap
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.party.Party
import de.hglabor.plugins.duels.party.Partys.isInParty
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.reset
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.async
import net.axay.kspigot.runnables.task
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

        fun createParty(party: Party, kit: Kits) {
            val tournament = Tournament()
            tournament.host = party.leader
            tournament.type = TournamentType.PARTY
            tournament.kit = kit
        }
    }

    lateinit var host: Player
    lateinit var kit: Kits
    lateinit var type: TournamentType
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
            timeToStart--
            if (timeToStart % 30 == 0) {
                val min = timeToStart / 60
                val sec = timeToStart % 60

                if (timeToStart == 0) {
                    broadcast(" xdd")
                    startDuels()
                    it.cancel()
                    return@task
                }
                onlinePlayers.forEach {
                    if (it.localization("de"))
                        it.sendMessage("${Localization.PREFIX}Das Turnier startet in ${KColors.MEDIUMPURPLE}$min${KColors.DARKGRAY}:${KColors.MEDIUMPURPLE}$sec ${(if (min == 1) "Minute" else "Minuten")}§7.")
                    else
                        it.sendMessage("${Localization.PREFIX}The tournament will start in ${KColors.MEDIUMPURPLE}$min${KColors.DARKGRAY}:${KColors.MEDIUMPURPLE}$sec ${(if (min == 1) "minute" else "minutes")}§7.")
                }
            }

        }
    }

    fun join(player: Player) {
        if (player.isInParty()) {
            val party = Party.get(player)
            if (party!!.players.size > teamSize) {
                party.delete()
                players.add(player)
                teams.add(arrayListOf(player))
                broadcast("${player.name} joined torurnamt")
            } else {
                players.addAll(party.players)
                teams.add(party.players)
                broadcast("${player.name}'s party joined torurnamt")
            }
        } else {
            players.add(player)
            teams.add(arrayListOf(player))
            broadcast("${player.name} joined torurnamt")
        }
    }

    fun startDuels() {
        broadcast("duels starting..")
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
                broadcast("duel found")
            } else {
                async {
                    teamlist.forEach {
                        it.forEach { players ->
                            players.sendMessage("kein gegner")
                        }
                        teamlist.remove(it)
                    }
                }
            }
        }
    }

    fun duelEnded(duel: Duel) {
        teams.remove(duel.loser)
        fightingTeams.remove(duel.loser)
        fightingTeams.remove(duel.winner)

        sendMessage("§4team died. ${teams.size} teams left")

        if (fightingTeams.isEmpty()) {
            roundEnded()
            broadcast("roudn ended")
        } else {
            broadcast("roudn still going")
        }
    }

    fun roundEnded() {
        broadcast("!")
        if (teams.size == 1) {
            endTournament()

        } else if (teams.size > 1) {
            broadcast("next round")
            startDuels()

        } else {
            broadcast("0 teams übrig??? XD")
        }

    }

    private fun endTournament() {
        broadcast("§d${teams.first()} win!")
        Tournaments.publicTournament = null

        async {
            players.forEach {
                it.reset()
            }
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
