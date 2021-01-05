package de.hglabor.plugins.staff.commands

import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import de.hglabor.plugins.staff.utils.BanUtils.banPlayer
import de.hglabor.plugins.staff.utils.StaffData.isStaff
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


object BanCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player = sender
            if (player.isStaff) {
                if (args.size >= 2) {
                    var reason = ""
                    for (i in 1 until args.size) reason += args[i] + " "
                    banPlayer(args[0], player, reason)
                    player.sendLocalizedMessage(Localization.PLAYER_WAS_BANNED_DE.replace("%playerName%", args[0]),
                        Localization.PLAYER_WAS_BANNED_EN.replace("%playerName%", args[0]))
                } else {
                    player.sendLocalizedMessage(Localization.COMMAND_WRONG_ARGUMENTS_DE, Localization.COMMAND_WRONG_ARGUMENTS_EN)
                    player.sendLocalizedMessage(Localization.BAN_COMMAND_HELP_DE, Localization.BAN_COMMAND_HELP_EN)
                }
            } else {
                player.sendLocalizedMessage(Localization.NO_PERM_DE, Localization.NO_PERM_EN)
            }
        }
        return false
    }

}