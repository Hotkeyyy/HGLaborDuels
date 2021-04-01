package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.duel.GameState
import de.hglabor.plugins.duels.guis.KitsGUI
import de.hglabor.plugins.duels.localization.sendMsg
import de.hglabor.plugins.duels.tournament.Tournaments
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.staff.utils.StaffData.isStaff
import net.axay.kspigot.gui.openGUI
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object TournamentCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (args.size == 1) {
                if (args[0].equals("create", true)) {
                    if (sender.isStaff) {
                        if (Tournaments.publicTournament == null) {
                            Data.openedKitInventory[sender] = KitsGUI.KitInventories.TOURNAMENT
                            sender.openGUI(KitsGUI.guiBuilder())
                        } else {
                            sender.sendMsg("tournament.fail.publicExists")
                        }
                    } else
                        sender.sendMsg("noPermission")
                    return true
                } else if (args[0].equals("join", true)) {
                    if (Tournaments.publicTournament?.players?.contains(sender) == false)
                        if (Tournaments.publicTournament?.state != GameState.INGAME)
                            Tournaments.publicTournament?.join(sender)
                        else
                            sender.sendMsg("tournament.fail.alreadyJoined")
                    return true

                } else if (args[0].equals("leave", true)) {
                    if (Tournaments.publicTournament?.players?.contains(sender) == true)
                        Tournaments.publicTournament?.leave(sender)
                    else
                        sender.sendMsg("tournament.fail.notInTournament")
                    return true
                } else if (args[0].equals("list", true)) {
                    sender.sendMessage("starting: ${Tournaments.publicTournament?.timeToStart}")
                    sender.sendMessage("teams: ${Tournaments.publicTournament?.teams?.size}")
                    sender.sendMessage("fighting teams : ${Tournaments.publicTournament?.fightingTeams?.size}")
                    sender.sendMessage("players: ${Tournaments.publicTournament?.players}")
                    return true
                }
            } else if (args.size == 2) {
                if (sender.isStaff) {
                    if (Tournaments.publicTournament?.state == GameState.COUNTDOWN)
                        if (args[0].equals("start", true)) {
                            Tournaments.publicTournament?.timeToStart = args[1].toInt()
                            Tournaments.publicTournament?.announce(false)
                            return true
                        }
                }
            }
        }
        return false
    }

}