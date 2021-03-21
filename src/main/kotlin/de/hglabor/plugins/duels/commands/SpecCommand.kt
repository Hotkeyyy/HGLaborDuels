package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.data.PlayerSettings
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.localization.sendMsg
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
            if (!sender.isInFight() && !sender.isInSoupsimulator()) {
                if (args.size == 1) {
                    val t = Bukkit.getPlayer(args[0])
                    if (t != null) {
                        if (t.isInFight()) {
                            if (PlayerSettings.get(t).ifAllowSpectators()) {
                                Data.duelFromSpec[sender]?.removeSpectator(sender, true)
                                Data.duelFromPlayer(t).addSpectator(sender, true)
                            } else {
                                sender.sendMsg("spec.fail.playerDenied", mutableMapOf("playerName" to t.name))
                            }
                        } else {
                            sender.sendMsg("spec.fail.playerNotFighting", mutableMapOf("playerName" to t.name))
                        }
                    } else {
                        sender.sendMsg("playerNotOnline", mutableMapOf("playerName" to args[0]))
                    }
                } else {
                    sender.sendMsg("spec.help")
                }
            } else {
                sender.sendMsg("command.cantExecuteNot")
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