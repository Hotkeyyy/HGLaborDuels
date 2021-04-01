package de.hglabor.plugins.staff.commands

import de.hglabor.plugins.duels.localization.sendMsg
import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.staff.Staffmode
import de.hglabor.plugins.staff.utils.StaffData.isStaff
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object StaffmodeCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val duelsPlayer = DuelsPlayer.get(sender)
            if (!duelsPlayer.isBusy()) {
                if (sender.isStaff) {
                    Staffmode.toggle(sender)
                } else {
                    sender.sendMsg("noPermission")
                }
            } else {
                sender.sendMsg("command.cantExecuteNow")
            }
        }
        return false
    }
}