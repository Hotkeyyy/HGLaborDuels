package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.data.PlayerSettings
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.soupsimulator.Soupsim.isInSoupsimulator
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import net.axay.kspigot.extensions.onlinePlayers
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.ArrayList

object SpecCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player = sender
            if (!player.isInFight() && !player.isInSoupsimulator()) {
                if (args.size == 1) {
                    val t = Bukkit.getPlayer(args[0])
                    if (t != null) {
                        if (t.isInFight()) {
                            if (PlayerSettings.get(t).ifAllowSpectators()) {
                                Data.duelFromSpec[player]?.removeSpectator(player, true)
                                Data.duelFromPlayer(t).addSpectator(player, true)
                            } else {
                                player.sendLocalizedMessage(
                                    Localization.SPEC_COMMAND_DENIED_DE,
                                    Localization.SPEC_COMMAND_DENIED_EN,
                                    "%playerName%",
                                    t.displayName
                                )
                            }
                        } else {
                            if (player.localization("de"))
                                player.sendMessage(
                                    Localization.SPEC_COMMAND_PLAYER_NOT_FIGHTING_DE.replace(
                                        "%playerName%", t.displayName
                                    )
                                )
                            else
                                player.sendMessage(
                                    Localization.SPEC_COMMAND_PLAYER_NOT_FIGHTING_EN.replace(
                                        "%playerName%", t.displayName
                                    )
                                )
                        }
                    } else {
                        if (player.localization("de"))
                            player.sendMessage(Localization.PLAYER_NOT_ONLINE_DE.replace("%playerName%", args[0]))
                        else
                            player.sendMessage(Localization.PLAYER_NOT_ONLINE_EN.replace("%playerName%", args[0]))
                    }
                } else {
                    if (player.localization("de"))
                        player.sendMessage(Localization.SPEC_COMMAND_HELP_DE)
                    else
                        player.sendMessage(Localization.SPEC_COMMAND_HELP_EN)
                }
            } else {
                if (player.localization("de"))
                    player.sendMessage(Localization.CANT_DO_THAT_RIGHT_NOW_DE)
                else
                    player.sendMessage(Localization.CANT_DO_THAT_RIGHT_NOW_EN)
            }
        }
        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String>? {
        val l: MutableList<String> = ArrayList()
        if (args.size == 1) {
            onlinePlayers.forEach {
                if (it.isInFight()) {
                    l.add(it.name)
                }
            }
            return l
        }
        return null
    }
}