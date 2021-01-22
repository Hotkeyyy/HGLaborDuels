package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.tournament.Tournament
import de.hglabor.plugins.duels.tournament.Tournaments
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object TournamentCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player = sender

            if (args.size == 1) {
                if (args[0].equals("create", true)) {
                    Tournament.createPublic(player, Kits.EHG)
                    return true
                } else if (args[0].equals("join", true)) {
                    Tournaments.publicTournament?.join(player)
                    return true
                } else if (args[0].equals("list", true)) {
                    player.sendMessage("starting: ${Tournaments.publicTournament?.timeToStart}")
                    player.sendMessage("teams: ${Tournaments.publicTournament?.teams?.size}")
                    player.sendMessage("fighting teams : ${Tournaments.publicTournament?.fightingTeams?.size}")
                    player.sendMessage("players: ${Tournaments.publicTournament?.players}")
                    return true
                }
            } else if (args.size == 2) {
                if (args[0].equals("start", true)) {
                    Tournaments.publicTournament!!.timeToStart = args[1].toInt()
                    return true
                }
            }
            player.sendMessage("/tournament create")
            player.sendMessage("/tournament join")
        }
        return false
    }

}