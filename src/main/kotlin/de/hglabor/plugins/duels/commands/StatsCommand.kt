package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.database.temporaryalternative.Stats
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import net.axay.kspigot.chat.KColors
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object StatsCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player = sender
            val target: UUID
            var targetIsOtherPlayer = false
            if (args.size == 1) {
                if (Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore()) {
                    target = Bukkit.getOfflinePlayer(args[0]).uniqueId
                    targetIsOtherPlayer = true
                } else {
                    if (player.localization("de")) {
                        player.sendMessage(
                            Localization.STATS_COMMAND_PLAYER_NOT_FOUND_DE.replace(
                                "%playerName%", args[0]
                            )
                        )
                        return false
                    } else {
                        player.sendMessage(
                            Localization.STATS_COMMAND_PLAYER_NOT_FOUND_EN.replace(
                                "%playerName%", args[0]
                            )
                        )
                        return false
                    }
                }
            } else {
                target = player.uniqueId
            }
            val stats = Stats(target)
            player.sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                         ")
            if (player.localization("de")) {
                if (targetIsOtherPlayer)
                    player.sendMessage(
                        Localization.STATS_COMMAND_STATS_OF_PLAYER_DE.replace(
                            "%playerName%", Bukkit.getOfflinePlayer(target).name!!
                        )
                    )

                player.sendMessage(
                    Localization.STATS_COMMAND_TOTAL_GAMES_DE.replace(
                        "%fights%", stats.getTotalGames().toString()
                    )
                )
                player.sendMessage(
                    Localization.STATS_COMMAND_KILLS.replace(
                        "%kills%", stats.getKills().toString()
                    )
                )
                player.sendMessage(
                    Localization.STATS_COMMAND_DEATHS_DE.replace(
                        "%deaths%", stats.getDeaths().toString()
                    )
                )
                if (stats.getDeaths() != 0)
                    player.sendMessage(
                        Localization.STATS_COMMAND_KD.replace(
                            "%kd%", (stats.getKills().toDouble() / stats.getDeaths().toDouble()).toString()
                        )
                    )
                else
                    player.sendMessage(
                        Localization.STATS_COMMAND_KD.replace(
                            "%kd%", (stats.getKills()).toString()
                        )
                    )
                player.sendMessage(
                    Localization.STATS_COMMAND_SOUPS_EATEN_DE.replace(
                        "%soupsEaten%", (stats.getSoupsEaten()).toString()
                    )
                )
                player.sendMessage(
                    Localization.STATS_COMMAND_TOTAL_HITS_DE.replace(
                        "%totalHits%", (stats.getTotalHits()).toString()
                    )
                )
            } else {
                if (targetIsOtherPlayer)
                    player.sendMessage(
                        Localization.STATS_COMMAND_STATS_OF_PLAYER_EN.replace(
                            "%playerName%", Bukkit.getOfflinePlayer(target).name!!
                        )
                    )

                player.sendMessage(
                    Localization.STATS_COMMAND_TOTAL_GAMES_EN.replace(
                        "%fights%", stats.getTotalGames().toString()
                    )
                )
                player.sendMessage(
                    Localization.STATS_COMMAND_KILLS.replace(
                        "%kills%", stats.getKills().toString()
                    )
                )
                player.sendMessage(
                    Localization.STATS_COMMAND_DEATHS_EN.replace(
                        "%deaths%", stats.getDeaths().toString()
                    )
                )
                if (stats.getDeaths() != 0)
                    player.sendMessage(
                        Localization.STATS_COMMAND_KD.replace(
                            "%kd%", (stats.getKills().toDouble() / stats.getDeaths().toDouble()).toString()
                        )
                    )
                else
                    player.sendMessage(
                        Localization.STATS_COMMAND_KD.replace(
                            "%kd%", (stats.getKills()).toString()
                        )
                    )
                player.sendMessage(
                    Localization.STATS_COMMAND_SOUPS_EATEN_EN.replace(
                        "%soupsEaten%", (stats.getSoupsEaten()).toString()
                    )
                )
                player.sendMessage(
                    Localization.STATS_COMMAND_TOTAL_HITS_EN.replace(
                        "%totalHits%", (stats.getTotalHits()).toString()
                    )
                )
                player.sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                         ")
            }
        }
        return false
    }
}