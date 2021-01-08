package de.hglabor.plugins.staff.commands

import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import de.hglabor.plugins.staff.utils.BanUtils.tempbanPlayer
import de.hglabor.plugins.staff.utils.StaffData.isStaff
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


object TempBanCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player = sender
            if (player.isStaff) {
                if (args.size >= 4) {
                    var reason = ""
                    val amount = args[1]
                    val timeunit = args[2].toLowerCase()

                    if (!isValidTime(amount, timeunit)) {
                        player.sendLocalizedMessage(Localization.COMMAND_WRONG_ARGUMENTS_DE,
                            Localization.COMMAND_WRONG_ARGUMENTS_EN)
                        player.sendLocalizedMessage(Localization.TEMPBAN_COMMAND_HELP_DE,
                            Localization.TEMPBAN_COMMAND_HELP_EN)
                        player.sendLocalizedMessage(Localization.TEMPBAN_COMMAND_TIMEUNITS_DE,
                            Localization.TEMPBAN_COMMAND_TIMEUNITS_EN)
                        return false
                    }

                    for (i in 3 until args.size) reason += args[i] + " "
                    tempbanPlayer(args[0], player, amount.toInt(), timeunit, reason)
                    player.sendLocalizedMessage(Localization.PLAYER_WAS_TEMPBANNED_DE.replace("%playerName%", args[0]),
                        Localization.PLAYER_WAS_TEMPBANNED_EN.replace("%playerName%", args[0]))
                } else {
                    player.sendLocalizedMessage(Localization.COMMAND_WRONG_ARGUMENTS_DE,
                        Localization.COMMAND_WRONG_ARGUMENTS_EN)
                    player.sendLocalizedMessage(Localization.TEMPBAN_COMMAND_HELP_DE,
                        Localization.TEMPBAN_COMMAND_HELP_EN)
                    player.sendLocalizedMessage(Localization.TEMPBAN_COMMAND_TIMEUNITS_DE,
                        Localization.TEMPBAN_COMMAND_TIMEUNITS_EN)
                }
            } else {
                sender.sendLocalizedMessage(Localization.NO_PERM_DE, Localization.NO_PERM_EN)
            }
        }

        return false
    }

    private fun isValidTime(amount: String, timeunit: String): Boolean {
        if (!isNumeric(amount)) return false
        when (timeunit) {
            "h", "w", "d", "m" -> return true
        }
        return false
    }

    private fun isNumeric(strNum: String?): Boolean {
        if (strNum == null) {
            return false
        }
        try {
            val d = strNum.toDouble()
        } catch (nfe: NumberFormatException) {
            return false
        }
        return true
    }
}