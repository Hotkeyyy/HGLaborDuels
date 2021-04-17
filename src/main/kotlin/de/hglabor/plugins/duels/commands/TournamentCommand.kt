package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.guis.KitsGUI
import de.hglabor.plugins.duels.tournament.Tournament.Companion.publicTournament
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.sendMsg
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
                when (args[0].toLowerCase()) {
                    "create" -> {
                        if (sender.isStaff) {
                            if (publicTournament == null) {
                                Data.openedKitInventory[sender] = KitsGUI.KitInventories.TOURNAMENT
                                sender.openGUI(KitsGUI.guiBuilder())
                            } else {
                                sender.sendMsg("tournament.fail.publicExists")
                            }
                        } else {
                            sender.sendMsg("noPermission")
                        }
                    }
                    "join" -> {
                        if (publicTournament?.players?.contains(sender) == false) {
                            if (publicTournament?.state != Data.GameState.INGAME) {
                                publicTournament?.join(sender)
                            } else {
                                sender.sendMsg("tournament.fail.alreadyJoined")
                            }
                        }
                    }
                    "leave" -> {
                        if (publicTournament?.players?.contains(sender) == true) {
                            publicTournament?.leave(sender)
                        } else {
                            sender.sendMsg("tournament.fail.notInTournament")
                        }
                    }
                    "list" -> {
                        sender.sendMessage("starting: ${publicTournament?.timeToStart}")
                        sender.sendMessage("teams: ${publicTournament?.teams?.size}")
                        sender.sendMessage("fighting teams: ${publicTournament?.fightingTeams?.size}")
                        var playersInTournament = ""
                        publicTournament?.players?.keys?.forEach { player ->
                            playersInTournament += player.name
                        }
                        sender.sendMessage("players in tournament: ${publicTournament?.players}")
                        var fightingPlayers = ""
                        publicTournament?.fightingTeams?.forEach { team ->
                            team.livingMembers.forEach { member ->
                                fightingPlayers += member.name
                            }
                        }
                        sender.sendMessage("players in fight: $fightingPlayers")
                    }
                }
            } else if (args.size == 2) {
                if (sender.isStaff) {
                    if (publicTournament?.state == Data.GameState.COUNTDOWN)
                        if (args[0].equals("start", true)) {
                            publicTournament?.timeToStart = args[1].toInt()
                            publicTournament?.announceTimer(false)
                            return true
                        }
                }
            }
        }
        return false
    }

}