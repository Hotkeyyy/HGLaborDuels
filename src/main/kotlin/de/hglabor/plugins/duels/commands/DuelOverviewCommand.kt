package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.guis.overview.DuelOverviewGUI
import de.hglabor.plugins.duels.localization.sendMsg
import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.duels.utils.Data
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object DuelOverviewCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val duelsPlayer = DuelsPlayer.get(sender)
            if (duelsPlayer.isBusy()) {
                sender.sendMsg("command.cantExecuteNow")
                return false
            }

            if (args.size == 1) {
                if (Data.gameIDs.contains(args[0])) {
                    DuelOverviewGUI.open(sender, args[0])
                } else {
                    sender.sendMsg("dueloverview.gameNotFound", mutableMapOf("gameID" to args[0]))
                }
            } else if (args.size == 1) {
                sender.sendMsg("dueloverview.help")
            }

        }
        return false
    }
}


