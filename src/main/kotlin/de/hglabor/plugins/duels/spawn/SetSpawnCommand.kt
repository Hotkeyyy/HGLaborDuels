package de.hglabor.plugins.duels.spawn

import de.hglabor.plugins.duels.localization.sendMsg
import de.hglabor.plugins.duels.spawn.SpawnUtils.setSpawn
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

