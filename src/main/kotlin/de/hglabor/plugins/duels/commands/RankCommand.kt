package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import de.hglabor.plugins.duels.utils.Ranks
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.onlinePlayers
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.util.ArrayList

object RankCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player = sender
            if (player.isOp) {
                if (args.size == 3) {
                    if (args[0].equals("set", true)) {
                        val target = Bukkit.getPlayer(args[1])
                        if (target != null) {
                            if (Ranks.enumContains(args[2].toUpperCase())) {
                                Ranks.setRank(target, Ranks.Rank.valueOf(args[2].toUpperCase()))
                                player.sendMessage("${Localization.PREFIX}Du hast §3${target.name} §7den Rang ${KColors.MEDIUMPURPLE}${args[2].toUpperCase()} §7gegeben.")
                            } else {
                                player.sendMessage("${Localization.PREFIX}Unknown Rank:")
                                player.sendMessage("§7Ranks: §4Owner§7, §cAdmin§7, §5Mod§7, §eHelper§7, §2NormiePlus§7, Normie")
                            }
                        } else {
                            player.sendLocalizedMessage(Localization.PLAYER_NOT_ONLINE_DE, Localization.PLAYER_NOT_ONLINE_EN)
                        }
                    } else {
                        player.sendLocalizedMessage(Localization.COMMAND_WRONG_ARGUMENTS_DE, Localization.COMMAND_WRONG_ARGUMENTS_EN)
                        player.sendMessage("${KColors.MEDIUMPURPLE}/rank set [player] [rank]")
                        player.sendMessage("${KColors.GRAY}Ranks: Owner, Admin, Mod, Helper, Normie+, Normie")
                    }
                } else {
                    player.sendLocalizedMessage(Localization.COMMAND_WRONG_ARGUMENTS_DE, Localization.COMMAND_WRONG_ARGUMENTS_EN)
                    player.sendMessage("${KColors.MEDIUMPURPLE}/rank set [player] [rank]")
                    player.sendMessage("${KColors.GRAY}Ranks: Owner, Admin, Mod, Helper, Normie+, Normie")
                }
            } else {
                player.sendLocalizedMessage(Localization.NO_PERM_DE, Localization.NO_PERM_EN)
            }
        }
        return false
    }

    fun onTabComplete(sender: CommandSender?, command: Command, alias: String?, args: Array<String?>): List<String>? {
        if (command.name.equals("rank", true)) {
            val l: MutableList<String> = ArrayList()
            if (args.size == 1)
                l.add("set")
            else if (args.size == 2 && args[0].equals("set", true))
                onlinePlayers.forEach { l.add(it.name) }
            else if (args.size == 3 && args[0].equals("set", true)) {
                l.add("normie")
                l.add("normieplus")
                l.add("helper")
                l.add("mod")
                l.add("admin")
                l.add("owner")
            }
            return l
        }
        return null
    }
}