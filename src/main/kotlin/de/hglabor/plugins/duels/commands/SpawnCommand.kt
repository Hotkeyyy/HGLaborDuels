package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.utils.sendMsg
import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.duels.utils.SpawnUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object SpawnCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val duelsPlayer = DuelsPlayer.get(sender)
            if (!duelsPlayer.isBusy()) {
                sender.teleport(SpawnUtils.getSpawn())
            } else {
                sender.sendMsg("command.cantExecuteNow")
            }
        }
        return false
    }

}