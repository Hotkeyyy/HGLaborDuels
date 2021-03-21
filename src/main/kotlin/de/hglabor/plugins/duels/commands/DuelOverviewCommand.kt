package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.guis.overview.DuelOverviewGUI
import de.hglabor.plugins.duels.localization.sendMsg
import de.hglabor.plugins.duels.soupsimulator.Soupsim.isInSoupsimulator
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object DuelOverviewCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (!sender.isInFight() && !sender.isInSoupsimulator()) {
                if (args.size == 1) {
                    if (Data.gameIDs.contains(args[0])) {
                        DuelOverviewGUI.open(sender, args[0])
                    } else {
                        sender.sendMsg("dueloverview.gameNotFound", mutableMapOf("gameID" to args[0]))
                    }
                } else if (args.size == 1) {
                    sender.sendMsg("dueloverview.help")
                }
            } else {
                sender.sendMsg("command.cantExecuteNow")
            }
        }
        return false
    }
}


