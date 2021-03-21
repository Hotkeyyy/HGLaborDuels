package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.localization.sendMsg
import de.hglabor.plugins.duels.soupsimulator.Soupsim.isInSoupsimulator
import de.hglabor.plugins.duels.spawn.SpawnUtils
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.duels.utils.PlayerFunctions.isSpectator
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object SpawnCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (!sender.isInFight() && !sender.isSpectator() && !sender.isInSoupsimulator()) {
                sender.teleport(SpawnUtils.getSpawn())
            } else {
                sender.sendMsg("command.cantExecuteNow")
            }
        }
        return false
    }

}