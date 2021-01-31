package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.duel.GameState
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.tournament.Tournaments
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import de.hglabor.plugins.staff.utils.StaffData.isStaff
import net.axay.kspigot.gui.openGUI
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
                    if (player.isStaff) {
                        if (Tournaments.publicTournament == null) {
                            Data.openedKitInventory[player] = Data.KitInventories.TOURNAMENT
                            player.openGUI(ChooseKitGUI.gui)
                        } else {
                            player.sendLocalizedMessage(Localization.TOURNAMENTCOMMAND_PUBLIC_TOURNAMENT_EXISTS_DE, Localization.TOURNAMENTCOMMAND_PUBLIC_TOURNAMENT_EXISTS_EN)
                        }
                    } else
                        player.sendLocalizedMessage(Localization.NO_PERM_DE, Localization.NO_PERM_EN)
                    return true
                } else if (args[0].equals("join", true)) {
                    if (Tournaments.publicTournament?.players?.contains(player) == false)
                        if (Tournaments.publicTournament?.state != GameState.RUNNING)
                            Tournaments.publicTournament?.join(player)
                    else
                        player.sendLocalizedMessage(Localization.TOURNAMENTCOMMAND_ALREADY_IN_DE, Localization.TOURNAMENTCOMMAND_ALREADY_IN_EN)
                    return true

                } else if (args[0].equals("leave", true)) {
                    if (Tournaments.publicTournament?.players?.contains(player) == true)
                        Tournaments.publicTournament?.leave(player)
                    else
                        player.sendLocalizedMessage(Localization.TOURNAMENTCOMMAND_NOT_IN_DE, Localization.TOURNAMENTCOMMAND_NOT_IN_EN)
                    return true
                } else if (args[0].equals("list", true)) {
                    player.sendMessage("starting: ${Tournaments.publicTournament?.timeToStart}")
                    player.sendMessage("teams: ${Tournaments.publicTournament?.teams?.size}")
                    player.sendMessage("fighting teams : ${Tournaments.publicTournament?.fightingTeams?.size}")
                    player.sendMessage("players: ${Tournaments.publicTournament?.players}")
                    return true
                }
            } else if (args.size == 2) {
                if (player.isStaff) {
                    if (Tournaments.publicTournament?.state == GameState.COUNTDOWN)
                        if (args[0].equals("start", true)) {
                           Tournaments.publicTournament?.timeToStart = args[1].toInt()
                            Tournaments.publicTournament?.announce(false)
                           return true
                        }
                }
            }
            player.sendMessage("/tournament create")
            player.sendMessage("/tournament join")
        }
        return false
    }

}