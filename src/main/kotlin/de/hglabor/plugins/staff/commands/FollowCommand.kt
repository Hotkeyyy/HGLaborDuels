package de.hglabor.plugins.staff.commands

import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.soupsimulator.isInSoupsimulator
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import de.hglabor.plugins.staff.Staffmode.follow
import de.hglabor.plugins.staff.Staffmode.unfollow
import de.hglabor.plugins.staff.utils.StaffData
import de.hglabor.plugins.staff.utils.StaffData.isInStaffMode
import de.hglabor.plugins.staff.utils.StaffData.isStaff
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object FollowCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (!sender.isInFight() && !sender.isInSoupsimulator()) {
                if (sender.isStaff) {
                    if (sender.isInStaffMode) {
                        if (args.size == 1) {
                            val target = Bukkit.getPlayer(args[0])
                            if (target != null) {
                                sender.follow(target)
                            } else {
                                sender.sendLocalizedMessage(Localization.STAFF_PLAYER_NOT_FOUND_DE, Localization.STAFF_PLAYER_NOT_FOUND_EN)
                            }
                        } else if (args.isEmpty()) {
                            if (StaffData.followedPlayerFromStaff.containsKey(sender)) {
                                sender.unfollow()
                            } else {
                                sender.sendLocalizedMessage(Localization.COMMAND_WRONG_ARGUMENTS_DE, Localization.COMMAND_WRONG_ARGUMENTS_DE)
                                sender.sendLocalizedMessage(Localization.FOLLOW_COMMAND_HELP_DE, Localization.FOLLOW_COMMAND_HELP_EN)
                            }
                        }
                    } else {
                        sender.sendLocalizedMessage(Localization.HAVE_TO_BE_IN_STAFFMODE_DE, Localization.HAVE_TO_BE_IN_STAFFMODE_EN)
                    }
                } else {
                    sender.sendLocalizedMessage(Localization.NO_PERM_DE, Localization.NO_PERM_EN)
                }
            } else {
                sender.sendLocalizedMessage(Localization.CANT_DO_THAT_RIGHT_NOW_DE, Localization.CANT_DO_THAT_RIGHT_NOW_EN)
            }
        }
        return false
    }
}