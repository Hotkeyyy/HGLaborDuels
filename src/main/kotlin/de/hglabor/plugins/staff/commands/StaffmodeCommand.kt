package de.hglabor.plugins.staff.commands

import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.localization.sendMsg
import de.hglabor.plugins.duels.soupsimulator.Soupsim.isInSoupsimulator
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import de.hglabor.plugins.staff.Staffmode
import de.hglabor.plugins.staff.utils.StaffData.isStaff
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object StaffmodeCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (!sender.isInFight() && !sender.isInSoupsimulator()) {
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