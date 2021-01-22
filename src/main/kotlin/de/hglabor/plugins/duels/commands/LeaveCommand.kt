package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.soupsimulator.Soupsim.isInSoupsimulator
import de.hglabor.plugins.duels.soupsimulator.Soupsimulator
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object LeaveCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (sender.isInSoupsimulator())
                Soupsimulator.get(sender)?.end()

            else if (sender.isInFight()) {
                val duel = Data.duelFromPlayer(sender)
                duel.playerLeft(sender)
            } else
                sender.sendLocalizedMessage(Localization.CANT_DO_THAT_RIGHT_NOW_DE, Localization.CANT_DO_THAT_RIGHT_NOW_EN)
        }
        return false
    }
}