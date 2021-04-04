package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.database.data.PlayerStats
import de.hglabor.plugins.duels.utils.sendMsg
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.runnables.async
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


object StatsCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            var targetName = sender.name

            if (args.size == 1) {
                targetName = args[0]
                if (!PlayerStats.exist(Bukkit.getOfflinePlayer(targetName).uniqueId)) {
                    sender.sendMsg("stats.fail.playerNotFound", mutableMapOf("playerName" to targetName))
                    return false
                }
            }

            async {
                val stats = PlayerStats.get(targetName)
                sender.sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                         ")
                sender.sendMsg("sound.player", mutableMapOf("playerName" to targetName))
                sender.sendMsg("sound.totalFights", mutableMapOf("totalFights" to stats.totalGames().toString()))
                sender.sendMsg("sound.kills", mutableMapOf("kills" to stats.kills().toString()))
                sender.sendMsg("sound.deaths", mutableMapOf("deaths" to stats.deaths().toString()))
                sender.sendMsg("sound.kd", mutableMapOf("kd" to stats.kd().toString()))
                sender.sendMsg(
                    "sound.soupsimulatorHighscore",
                    mutableMapOf("soupsimulatorHighscore" to stats.soupsimulatorHighscore().toString())
                )
                sender.sendMsg("sound.soupsEaten", mutableMapOf("soupsEaten" to stats.soupsEaten().toString()))
                sender.sendMsg("sound.totalHits", mutableMapOf("totalHits" to stats.totalHits().toString()))
                sender.sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                         ")
            }
        }
        return false
    }
}