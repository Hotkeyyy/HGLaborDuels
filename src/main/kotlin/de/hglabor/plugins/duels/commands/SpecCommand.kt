package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.utils.sendMsg
import de.hglabor.plugins.duels.player.DuelsPlayer
import net.axay.kspigot.extensions.onlinePlayers
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.*

object SpecCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val duelsPlayer = DuelsPlayer.get(sender)
            if (!duelsPlayer.isBusy()) {
                if (args.size == 1) {
                    val target = Bukkit.getPlayer(args[0])
                    if (target != null) {
                        val duelsTarget = DuelsPlayer.get(target)
                        if (duelsTarget.isInFight()) {
                            if (duelsTarget.settings.ifAllowSpectators()) {
                                val duel = duelsTarget.currentDuel() ?: return false
                                duelsPlayer.spectateDuel(duel, true)
                            } else {
                                sender.sendMsg("spec.fail.playerDenied", mutableMapOf("playerName" to target.name))
                            }
                        } else {
                            sender.sendMsg("spec.fail.playerNotFighting", mutableMapOf("playerName" to target.name))
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
                if (DuelsPlayer.get(it).isInFight()) {
                    l.add(it.name)
                }
            }
            return l
        }
        return null
    }
}