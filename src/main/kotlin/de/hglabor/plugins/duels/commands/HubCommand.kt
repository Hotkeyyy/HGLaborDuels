package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.Manager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

object HubCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player = sender
            val bytearray = ByteArrayOutputStream();
            val out = DataOutputStream(bytearray);
            try {
                out.writeUTF("Connect")
                out.writeUTF("lobby")
            } catch (e: IOException) {
                e.printStackTrace()
            }
            player.sendPluginMessage(Manager.INSTANCE, "BungeeCord", bytearray.toByteArray())
        }
        return false
    }

}