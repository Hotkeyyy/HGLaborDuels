package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.data.PlayerStats
import de.hglabor.plugins.duels.database.MongoManager
import de.hglabor.plugins.duels.localization.Localization
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

            /* player.sendMessage("${KColors.TOMATO}Stats are disbaled until the final release")
            return false*/

            if (args.size == 1) {
                if (args[0].equals("reset", true)) {
                    MongoManager.playerStatsCollection.drop()
                    return true
                }

                if (args[0].equals("yes", true)) {
                    player.sendMessage(PlayerStats.get(player).toDocument().toString())
                    return true
                }

                if (args[0].equals("add", true)) {
                    player.sendMessage("d")
                    return true
                }

                targetName = args[0]
                target = Bukkit.getOfflinePlayer(targetName)
                if (PlayerStats.exist(target.uniqueId)) {
                    targetIsOtherPlayer = true
                } else {
                    player.sendLocalizedMessage(
                        Localization.STATS_COMMAND_PLAYER_NOT_FOUND_DE.replace("%playerName%", target.name!!),
                        Localization.STATS_COMMAND_PLAYER_NOT_FOUND_EN.replace("%playerName%", target.name!!)
                    )
                    return false
                }
            } else {
                targetName = player.name
                target = player
            }

            player.sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                         ")
            if (player.localization("de")) {
                if (targetIsOtherPlayer)
                    player.sendMessage(" §8| §7Stats von Spieler §(» ${KColors.MEDIUMPURPLE}${target.name}")
                sendStatsDE(player, targetName)
            } else {
                if (targetIsOtherPlayer)
                    player.sendMessage(" §8| §7Stats of player §(» ${KColors.MEDIUMPURPLE}${target.name}")
                sendStatsEN(player, targetName)
            }
        }
        return false
    }

    private fun sendStatsDE(player: Player, targetName: String) {
        async {
            val stats = PlayerStats.get(targetName)
            player.sendMessage(" §8| §7Gesamte Spiele §8» ${KColors.DEEPSKYBLUE}${stats.totalGames()}")
            player.sendMessage(" §8| §7Kills §8» ${KColors.DEEPSKYBLUE}${stats.kills()}")
            player.sendMessage(" §8| §7Tode §8» ${KColors.DEEPSKYBLUE}${stats.deaths()}")
            player.sendMessage(" §8| §7K/D §8» ${KColors.DEEPSKYBLUE}${stats.kd()}")
            player.sendMessage(" §8| §7Soupsimulator Rekord §8» ${KColors.SPRINGGREEN}${stats.soupsimulatorHighscore()}")
            player.sendMessage(" §8| §7Gegessene Suppen §8» ${KColors.DODGERBLUE}${stats.soupsEaten()}")
            player.sendMessage(" §8| §7Gesamte Schläge §8» ${KColors.DODGERBLUE}${stats.totalHits()}")
            player.sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                         ")
        }

    }

    private fun sendStatsEN(player: Player, targetName: String) {
        async {
            val stats = PlayerStats.get(targetName)
            player.sendMessage(" §8| §7Total Games §8» ${KColors.DEEPSKYBLUE}${stats.totalGames()}")
            player.sendMessage(" §8| §7Kills §8» ${KColors.DEEPSKYBLUE}${stats.kills()}")
            player.sendMessage(" §8| §7Deaths §8» ${KColors.DEEPSKYBLUE}${stats.deaths()}")
            player.sendMessage(" §8| §7K/D §8» ${KColors.DEEPSKYBLUE}${stats.kd()}")
            player.sendMessage(" §8| §7Soupsimulator Highscore §8» ${KColors.SPRINGGREEN}${stats.soupsimulatorHighscore()}")
            player.sendMessage(" §8| §7Soups eaten §8» ${KColors.DODGERBLUE}${stats.soupsEaten()}")
            player.sendMessage(" §8| §7Total hits §8» ${KColors.DODGERBLUE}${stats.totalHits()}")
            player.sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                         ")
        }

    }

}