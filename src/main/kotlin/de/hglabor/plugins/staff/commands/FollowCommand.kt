package de.hglabor.plugins.staff.commands

import de.hglabor.plugins.duels.utils.sendMsg
import de.hglabor.plugins.duels.player.DuelsPlayer
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
            val duelsPlayer = DuelsPlayer.get(sender)
            if (duelsPlayer.isBusy()) {
                sender.sendMsg("command.cantExecuteNow")
                return false
            }

            if (sender.isStaff) {
                if (sender.isInStaffMode) {
                    if (args.size == 1) {
                        val target = Bukkit.getPlayer(args[0])
                        if (target != null) {
                            sender.follow(target)
                        } else {
                            sender.sendMsg("staff.playerNotFound")
                        }
                    } else if (args.isEmpty()) {
                        if (StaffData.followedPlayerFromStaff.containsKey(sender)) {
                            sender.unfollow()
                        } else {
                            sender.sendMsg("command.wrongArguments")
                            sender.sendMsg("staff.follow.help")
                        }
                    }
                } else {
                    sender.sendMsg("staff.staffmodeIsRequired")
                }
            } else {
                sender.sendMsg("noPermission")
            }
        }
        return false
    }
}