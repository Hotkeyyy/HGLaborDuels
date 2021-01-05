package de.hglabor.plugins.staff.commands

import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.soupsimulator.isInSoupsimulator
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
                    sender.sendLocalizedMessage(Localization.NO_PERM_DE, Localization.NO_PERM_EN)
                }
            } else {
                sender.sendLocalizedMessage(Localization.CANT_DO_THAT_RIGHT_NOW_DE, Localization.CANT_DO_THAT_RIGHT_NOW_EN)
            }
        }
        return false
    }
}