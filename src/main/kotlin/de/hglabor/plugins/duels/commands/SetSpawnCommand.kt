package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.utils.SpawnUtils.setSpawn
import de.hglabor.plugins.duels.utils.sendMsg
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object SetSpawnCommand : CommandExecutor {


    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (sender.isOp) {
                setSpawn(sender)
            } else {
                sender.sendMsg("noPermission")
            }
        }
        return false
    }
}

