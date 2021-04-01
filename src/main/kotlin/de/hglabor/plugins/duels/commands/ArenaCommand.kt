package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.arenas.Arenas
import de.hglabor.plugins.duels.arenas.CreateArena
import de.hglabor.plugins.duels.arenas.arenaFromPlayer
import de.hglabor.plugins.duels.functionality.CreateArenaInventory
import de.hglabor.plugins.duels.guis.CreateArenaGUI
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.localization.sendMsg
import de.hglabor.plugins.duels.player.DuelsPlayer
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.gui.openGUI
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.*

object ArenaCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player = sender
            val duelsPlayer = DuelsPlayer.get(player)
            if (!player.hasPermission("duels.createarena")) {
                player.sendMsg("noPermission")
                return false
            }
            if (duelsPlayer.isBusy()) {
                player.sendMsg("command.cantExecuteNow")
                return false
            }

            if (args.size == 1) {
                if (args[0].equals("create", true)) {
                    if (!arenaFromPlayer.containsKey(player)) {
                        arenaFromPlayer[player] = CreateArena(player)
                        CreateArenaInventory.giveItems(player)
                    }
                    player.openGUI(CreateArenaGUI.guiBuilder(player))
                    return true
                } else if (args[0].equals("buildworld", true)) {
                    player.teleport(Location(Bukkit.getWorld("BuildWorld"), 0.0, 5.0, 0.0))
                    return true
                } else if (args[0].equals("fightworld", true)) {
                    player.teleport(Location(Bukkit.getWorld("FightWorld"), 0.0, 150.0, 0.0))
                    return true
                } else if (args[0].equals("list", true)) {
                    val arenas = arrayListOf<String>()
                    var message = ""
                    for (arena in Arenas.allArenas.keys) {
                        arenas.add(arena)
                    }

                    for (i in 0 until arenas.size) {
                        message += arenas[i]
                        if (i < arenas.size)
                            message += ", "
                    }

                    player.sendMessage("${Localization.PREFIX}Arenas ${KColors.DARKGRAY}» ${KColors.GRAY}$message")
                    return true
                }
            }
            player.sendMsg("command.wrongArguments")
            player.sendMsg("arena.help.1")
            player.sendMsg("arena.help.2")
            player.sendMsg("arena.help.3")

        }
        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String>? {
        if (command.name.equals("arena", true)) {
            val l: MutableList<String> = ArrayList()
            if (args.size == 1) {
                l.add("create")
                l.add("arenaworld")
                l.add("buildworld")
                l.add("list")
            }
            return l
        }
        return null
    }
}


