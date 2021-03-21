package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.data.PlayerStats
import de.hglabor.plugins.duels.database.MongoManager
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.localization.sendMsg
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.runnables.async
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


object StatsCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player = sender
            val targetName: String
            var target: OfflinePlayer
            var targetIsOtherPlayer = false

            if (args.size == 1) {
                targetName = args[0]
                target = Bukkit.getOfflinePlayer(targetName)
                if (PlayerStats.exist(target.uniqueId)) {
                    targetIsOtherPlayer = true
                } else {
                    player.sendMsg("stats.fail.playerNotFound", mutableMapOf("playerName" to targetName))
                    return false
                }
            } else {
                targetName = player.name
                target = player
            }

            async {
                val stats = PlayerStats.get(targetName)
                player.sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                         ")
                player.sendMsg("stats.ofPlayer", mutableMapOf(
                    "playerName" to targetName,
                    "totalFights" to stats.totalGames().toString(),
                    "kills" to stats.kills().toString(),
                    "deaths" to stats.deaths().toString(),
                    "kd" to stats.kd().toString(),
                    "soupsimulatorHighscore" to stats.soupsimulatorHighscore().toString(),
                    "soupsEaten" to stats.soupsEaten().toString(),
                    "totalHits" to stats.totalHits().toString(),
                    ))
                player.sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                         ")
            }
        }
        return false
    }
}